package kitchenposNew.order.domain;

import kitchenpos.domain.OrderTable;
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
    private OrderTable orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(Long id, OrderTable orderTableId, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
        orderLineItems.forEach(
                orderLineItem -> orderLineItem.registerOrder(this)
        );
    }

    public Order(OrderTable orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
        orderLineItems.forEach(
                orderLineItem -> orderLineItem.registerOrder(this)
        );
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isOrderStatus(OrderStatus completion) {
        return this.orderStatus == completion;
    }

    public void setOrderLineItems(List<OrderLineItem> allByOrderId) {
        this.orderLineItems = allByOrderId;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
