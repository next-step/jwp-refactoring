package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.common.error.ErrorEnum;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private Long orderTableId;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        if (orderTableId == null) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_NOT_FOUND.message());
        }
        orderLineItems.updateOrder(this);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderLineItems);
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(null, orderTableId, orderLineItems);
    }

    public void validateOrderStatusShouldComplete() {
        if (!isCompletion()) {
            throw new IllegalArgumentException(ErrorEnum.NOT_PAYMENT_ORDER.message());
        }
    }

    public boolean isCompletion() {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
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

    public void setOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_COMPLETION_STATUS_NOT_CHANGE.message());
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
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
}
