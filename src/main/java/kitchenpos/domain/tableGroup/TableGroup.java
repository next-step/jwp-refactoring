package kitchenpos.domain.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.tableGroup.exceptions.InvalidTableGroupTryException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTableInTableGroup> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTableInTableGroup> orderTables) {
        validate(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTableInTableGroup> orderTables) {
        this(null, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableInTableGroup> getOrderTables() {
        return orderTables;
    }

    private void validate(final List<OrderTableInTableGroup> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidTableGroupTryException("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
        }
    }
}
