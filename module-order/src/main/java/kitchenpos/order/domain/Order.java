package kitchenpos.order.domain;

import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order(Long orderTableId) {
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    protected Order() {
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems.addAll(this, orderLineItems);
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemsException();
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        requireNonNull(orderStatus, "orderStatus");
        if (isCompleted()) {
            throw new CannotChangeOrderStatusException();
        }
        this.orderStatus = orderStatus;
    }

    private boolean isCompleted() {
        return OrderStatus.COMPLETION == this.orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }
}
