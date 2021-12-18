package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime = LocalDateTime.now();
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(id, orderTable, orderStatus, orderLineItems);
        this.orderedTime = orderedTime;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(orderTable, orderLineItems);
        this.orderStatus = orderStatus;
        this.id = id;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isStarted() {
        return orderStatus == OrderStatus.MEAL || orderStatus == OrderStatus.COOKING;
    }

    public void addOrderLineItem(Menu menu, long quantity) {
        orderLineItems.add(this, menu, quantity);
    }

    public boolean isComplete() {
        return OrderStatus.COMPLETION == orderStatus;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isComplete()) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
