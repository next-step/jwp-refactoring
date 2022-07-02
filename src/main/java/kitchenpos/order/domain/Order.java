package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(this, orderLineItems);
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems(this, orderLineItems);
    }

    public void changeOrderStatus(String status) {
        if (orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException();
        }
        orderStatus = OrderStatus.valueOf(status);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
