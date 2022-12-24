package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.OrderTable;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MIN_NUMBER_OF_GROUP = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        validateTableGroup();
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.occupied();
        }
    }

    private void validateTableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_NUMBER_OF_GROUP) {
            throw new IllegalArgumentException("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.onCookingOrMeal()) {
                throw new IllegalArgumentException("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
            }
            orderTable.setTableGroupId(null);
        }
    }
}
