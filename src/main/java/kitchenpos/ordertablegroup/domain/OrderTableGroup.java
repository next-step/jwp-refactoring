package kitchenpos.ordertablegroup.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;

import javax.persistence.*;

@Entity(name = "table_group")
public class OrderTableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected OrderTableGroup() {
    }

    public OrderTableGroup(OrderTables orderTables) {
        this.orderTables = orderTables;
        this.orderTables.setOrderTableGroup(this);
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void unGroup() {
        orderTables.getOrderTables().forEach(OrderTable::ungroupTable);
    }
}
