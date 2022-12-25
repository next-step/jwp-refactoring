package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.common.ErrorMessage;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_CANNOT_CHANGE_COMPLETION_ORDER.getMessage());
        }
        this.orderStatus = orderStatus;
    }

    public void checkCookingOrMeal() {
        if(orderStatus.isCooking() || orderStatus.isMeal()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_CANNOT_BE_CHANGED.getMessage());
        }
    }

    public void order(List<OrderLineItem> items) {
        items.forEach(orderLineItem -> this.orderLineItems.addOrderLineItem(this, orderLineItem));
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.values();
    }
}
