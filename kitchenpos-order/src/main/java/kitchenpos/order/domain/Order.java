package kitchenpos.order.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.exception.ErrorMessage;

@Entity
@Table(name = "orders")
public class Order {

    public static String ENTITY_NAME = "주문";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    private Order(Long orderTableId, OrderLineItems orderLineItems) {
        orderLineItems.updateOrder(this);
        this.orderTableId = orderTableId;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        validateCurrentOrderStatus();
        this.orderStatus = orderStatus;
    }

    public boolean isFinished() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }

    public Long getId() {
        return id;
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

    public Long getOrderTableId() {
        return orderTableId;
    }

    private void validateCurrentOrderStatus() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_CHANGE_ORDER_STATUS_WHEN_COMPLETED);
        }
    }

}
