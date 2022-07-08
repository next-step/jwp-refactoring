package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.order.dto.OrderLineItemResponse;

@Entity
@Table(name = "orders")
public class Order {

    public static final String TARGET_ORDER_STATUS_IS_INVALID_ERROR_MESSAGE = "변경하는 주문 상태가 올바르지 않습니다.";
    public static final String TARGET_ORDER_STATUS_IS_ALREADY_COMPLETION_ERROR_MESSAGE = "완료 상태인 주문의 주문 상태는 변경할 수 없습니다.";
    public static final String EMPTY_ORDERED_DATE_ERROR_MESSAGE = "주문 시간 정보는 필수 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    private OrderLineItems orderLineItems;

    protected Order() {

    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        validateOrder(orderedTime);
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.of(this, orderLineItems);
    }

    private void validateOrder(LocalDateTime orderedTime) {
        validateOrderedDateTime(orderedTime);
    }

    private void validateOrderedDateTime(LocalDateTime orderedTime) {
        if (orderedTime == null) {
            throw new IllegalArgumentException(EMPTY_ORDERED_DATE_ERROR_MESSAGE);
        }
    }

    public void changeOrderStatus(OrderStatus targetOrderStatus) {
        validateChangeTargetOrderStatus(targetOrderStatus);
        this.orderStatus = targetOrderStatus;
    }

    private void validateChangeTargetOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            throw new IllegalArgumentException(TARGET_ORDER_STATUS_IS_INVALID_ERROR_MESSAGE);
        }
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(TARGET_ORDER_STATUS_IS_ALREADY_COMPLETION_ERROR_MESSAGE);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public List<OrderLineItemResponse> toOrderLineItemResponse() {
        return orderLineItems.toResponse();
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
