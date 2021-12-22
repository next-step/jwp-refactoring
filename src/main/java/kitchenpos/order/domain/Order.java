package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;

@Entity(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    private final LocalDateTime orderedTime = LocalDateTime.now();

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        initOrderStatus(orderStatus);
        addOrderLineItems(orderLineItems);
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            this.orderLineItems.add(orderLineItem);
        }
    }

    public boolean isOnGoing() {
        return OrderStatus.COOKING.equals(orderStatus) || OrderStatus.MEAL.equals(orderStatus);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;

    }

    private void initOrderStatus(OrderStatus orderStatus) {
        if (Objects.isNull(orderStatus)) {
            orderStatus = OrderStatus.COOKING;
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatusName() {
        return orderStatus.name();
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

    public boolean isCompletion() {
        return OrderStatus.COMPLETION.equals(this.orderStatus);
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", orderTableId=" + orderTableId +
            ", orderedTime=" + orderedTime +
            ", orderStatus=" + orderStatus +
            ", orderLineItems=" + orderLineItems +
            '}';
    }
}
