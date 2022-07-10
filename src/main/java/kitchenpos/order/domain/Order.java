package kitchenpos.order.domain;

import kitchenpos.order.exception.EmptyOrderLineItemException;
import kitchenpos.order.exception.OrderAlreadyCompletedException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
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
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
    }

    public Order(Long id, Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
    }

    public void changeOrderStatus(final String orderStatus) {
        changeOrderStatus(OrderStatus.valueOf(orderStatus));
    }

    private void changeOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new OrderAlreadyCompletedException();
        }
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateEmptyOrderLineItems(orderLineItems);
        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void validateEmptyOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemException();
        }
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addedBy(this);
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
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
