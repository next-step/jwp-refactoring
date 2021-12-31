package kitchenpos.order.domain;

import kitchenpos.order.exception.CanNotChangeOrderStatusException;
import kitchenpos.order.validator.OrderCreateValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    private static final String COMPLETION_CHANGE_ORDER_STATUS_ERROR_MESSAGE = "완료된 주문은 주문상태를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Embedded
    private OrderLineItemGroup orderLineItems;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = OrderLineItemGroup.of(orderLineItems);
    }

    public Order(long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(orderTableId, orderStatus, orderLineItems);
        this.id = id;
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public static Order generate(long id, long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, OrderStatus.MEAL, orderLineItems);
    }

    public static Order generate(long id, long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public static Order create(Long orderTableId, List<OrderLineItem> orderLineItems, List<OrderCreateValidator> validators) {
        final Order order = Order.of(orderTableId, orderLineItems);
        for (OrderCreateValidator validator : validators) {
            validator.validate(order);
        }
        return order;
    }

    public void updateOrderStatus(String orderStatus) {
        if (isCompletion()) {
            throw new CanNotChangeOrderStatusException(COMPLETION_CHANGE_ORDER_STATUS_ERROR_MESSAGE);
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus);
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

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public List<Long> getMenuIds() {
        return this.orderLineItems.getMenuIds();
    }
}
