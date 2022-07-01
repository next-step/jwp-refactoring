package kitchenpos.table.domain;

import kitchenpos.order.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {

    }

    public TableGroup(Long id, OrderTables orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public void addOrderTables(OrderTables orderTables) {
        this.orderTables.addOrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
