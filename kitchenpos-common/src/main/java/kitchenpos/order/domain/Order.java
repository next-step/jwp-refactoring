package kitchenpos.order.domain;

import kitchenpos.order.OrderStatus;
import kitchenpos.order.handler.OrderValidator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems.registerOrder(this);
    }

    public Order(Long orderTableId, OrderLineItems orderLineItems) {
        this(null, orderTableId, orderLineItems);
    }

    public void validateOrder(OrderValidator orderValidator) {
        orderValidator.validateOrder(this);
        progressCook();
    }

    public void progressCook() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public boolean isCookingOrMeal() {
        return this.orderStatus.isCookingOrMeal();
    }

    public void changeOrderStatusCooking() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public void changeOrderStatusComplete() {
        this.orderStatus = OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return this.orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus);
    }
}
