package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class TableGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {}

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.values().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::checkForChangingOrderTable);
        this.orderTables.ungroup();
    }

    public void group(List<OrderTable> target) {
        this.orderTables.group(this, target);
    }

    public void addOrderTable(OrderTable orderTable) {
        this.orderTables.addOrderTable(this, orderTable);
    }
}
