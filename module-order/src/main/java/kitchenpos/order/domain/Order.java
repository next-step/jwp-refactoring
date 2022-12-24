package kitchenpos.order.domain;

import kitchenpos.order.application.OrderValidator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.order.application.OrderService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;


@Entity
@Table(name = "orders")
public class Order {
    public static final String ORDER_TABLE_NULL_EXCEPTION_MESSAGE = "주문 테이블이 없습니다.";
    public static final String COMPLETION_CHANGE_EXCEPTION_MESSAGE = "완료일 경우 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order(Long orderTableId, OrderLineItems orderLineItems) {
        validate(orderTableId, orderLineItems);
        this.orderTableId = orderTableId;
        orderLineItems.mapOrder(this);
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        validate(orderTableId, orderLineItems);
        this.orderTableId = orderTableId;
        orderLineItems.mapOrder(this);
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.id = id;
    }

    public Order(long id, Long orderTableId, OrderLineItems orderLineItems, OrderStatus status) {
        validate(orderTableId, orderLineItems);
        this.orderTableId = orderTableId;
        orderLineItems.mapOrder(this);
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.id = id;
        this.orderStatus = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void meal() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(COMPLETION_CHANGE_EXCEPTION_MESSAGE);
        }
        this.orderStatus = OrderStatus.MEAL;
    }

    public void complete() {
        this.orderStatus = OrderStatus.COMPLETION;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems.getOrderLineItems();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateCompleteOrderStatus();
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    private void validate(Long orderTableId, OrderLineItems orderLineItems) {
        validateOrderTable(orderTableId);
        validateEmptyOrderLineItems(orderLineItems);
    }

    private static void validateOrderTable(Long orderTableId) {
        validateOrderTableNull(orderTableId);
    }

    private void validateEmptyOrderLineItems(OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
        }
    }

    private static void validateOrderTableNull(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
        }
    }

    private void validateCompleteOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException(COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE);
        }
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public boolean isComplete() {
        return this.orderStatus.equals(OrderStatus.COMPLETION);
    }

    public void validate(OrderValidator orderValidator) {
        orderValidator.validate(this);
    }
}
