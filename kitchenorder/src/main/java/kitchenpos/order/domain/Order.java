package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.orderconstants.OrderErrorMessages;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {}

    public Order(Long orderTableId) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
            OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, orderStatus, orderedTime, new OrderLineItems(orderLineItems));
    }

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTableId);
        orderLineItems.forEach(order::addOrderLineItem);
        return order;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public boolean isOrderStatusNotComplete() {
        return orderStatus.equals(OrderStatus.COOKING) || orderStatus.equals(OrderStatus.MEAL);
    }

    public boolean isOrderStatusComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
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

    public String getOrderStatusName() {
        return orderStatus.name();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        checkOrderStatusChangeAble();
        this.orderStatus = orderStatus;
    }

    public void updateOrderStatus(String orderStatus) {
        updateOrderStatus(OrderStatus.valueOf(orderStatus));
    }

    private void checkOrderStatusChangeAble() {
        if (isOrderStatusComplete()) {
            throw new IllegalArgumentException(OrderErrorMessages.CANNOT_CHANGE_STATUS_OF_COMPLETED_ORDER);
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId)
                && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime)
                && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
