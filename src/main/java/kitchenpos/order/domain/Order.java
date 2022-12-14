package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

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
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();
    private Long orderTableId;

    protected Order() {}

    public Order(
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(
            Long id,
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            OrderLineItems orderLineItems
    ) {
        this(orderTableId, orderStatus, orderedTime);
        this.id = id;
        this.orderLineItems = orderLineItems;
    }

    public void validateOrderStatusShouldComplete() {
        if (!OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
        }
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

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateOrderStatus(OrderStatus status) {
        validateUpdateOrderStatus();
        this.orderStatus = status;
    }

    private void validateUpdateOrderStatus() {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_STATUS_COMPLETE.getMessage());
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

    public void setOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
        orderLineItems.setOrder(this);
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
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                ", orderLineItems=" + orderLineItems +
                ", orderTableId=" + orderTableId +
                '}';
    }
}
