package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {

    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, OrderTables orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        orderTables.forEach(it -> addOrderTable(it));
    }

    public void addOrderTable(OrderTable orderTable) {
        this.orderTables.addOrderTable(this, orderTable);
    }

    public void group(List<OrderTable> target) {
        if (target.size() <= 1) {
            throw new IllegalArgumentException();
        }
        orderTables.group(this, target);
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::checkOngoingOrderTable);
        this.orderTables.ungroup();
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
