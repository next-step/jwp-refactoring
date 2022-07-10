package kitchenpos.domain;

import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderExceptionType;
import org.springframework.util.CollectionUtils;

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
    @Column(name = "order_table_id")
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;
    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public Order(final Long id, final Long orderTableId) {
        this(id, orderTableId, OrderStatus.COOKING);
    }

    private Order(final Long orderTableId) {
        this(null, orderTableId);
    }

    public static Order of(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        validateItemEmpty(orderLineItems);
        final Order order = new Order(orderTableId);
        order.addOrderLineItems(orderLineItems);

        return order;
    }

    private static void validateItemEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderException(OrderExceptionType.NOT_EXIST_ORDER_ITEM);
        }
    }

    void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        orderLineItems
                .forEach(this::addOrderLineItem);
    }

    void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItem.updateOrder(this);
        this.orderLineItems.add(orderLineItem);
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTableId=" + orderTableId +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                ", orderLineItems=" + orderLineItems +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }
}
