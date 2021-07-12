package kitchenposNew.order.domain;

import kitchenposNew.order.OrderStatus;

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
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long id, OrderTable orderTableId, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems.registerOrder(this);
    }

    public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems.registerOrder(this);
    }

    public void changeOrderStatus() {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = OrderStatus.COOKING;
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
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTable, order.orderTable) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus);
    }
}
