package kitchenpos.order.domain;

import kitchenpos.exception.OrderError;
import kitchenpos.exception.OrderLineItemError;
import kitchenpos.exception.OrderTableError;
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

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {

    }

    private Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(null, orderTableId, orderLineItems);
    }

    public void order(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(OrderLineItemError.CANNOT_EMPTY);
        }

        orderLineItems.forEach(this::addOrderLineItem);
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkOngoingOrderTable() {
        if (orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL) {
            throw new IllegalArgumentException(OrderError.CANNOT_CHANGE);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException(OrderError.CANNOT_CHANGE_STATUS);
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
