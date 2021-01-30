package kitchenpos.order;

import kitchenpos.BaseEntity;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();
    */
    private List<Long> orderTableIds = new ArrayList();

    public TableGroup() {
    }
    public TableGroup(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문테이블이 비어있거나 주문테이블이 2미만이면 주문등록할 수 없습니다!");
        }

        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void compareOrderTables(List<Long> orderTables, List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문테이블과 저장된 테이블의 크기가 같지 않으면 주문등록할 수 없습니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("저장된 주문테이블이 비워있지 않거나 단체지정이 있으면 주문등록할 수 없습니다.");
            }
        }
    }

    public void changeOrderTablesIds(List<Long> savedOrderTables) {
        orderTableIds = savedOrderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
