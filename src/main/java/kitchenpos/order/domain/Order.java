package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemResponse;

@Entity
@Table(name = "orders")
public class Order {

    public static final String EMPTY_ORDER_TABLE_ERROR_MESSAGE = "주문 테이블이 비어있는 경우 주문을 생성할 수 없습니다.";
    public static final String EMPTY_ORDER_LINE_ITEM_ERROR_MESSAGE = "주문 항목이 없는 경우 주문을 생성할 수 없습니다.";
    public static final String TARGET_ORDER_STATUS_IS_INVALID_ERROR_MESSAGE = "변경하는 주문 상태가 올바르지 않습니다.";
    public static final String TARGET_ORDER_STATUS_IS_ALREADY_COMPLETION_ERROR_MESSAGE = "완료 상태인 주문의 주문 상태는 변경할 수 없습니다.";
    public static final String EMPTY_ORDERED_DATE_ERROR_MESSAGE = "주문 시간 정보는 필수 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "order_table_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_TO_ORDER_TABLE"),
        nullable = false
    )
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    private OrderLineItems orderLineItems;

    protected Order() {

    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        validateOrder(orderTable, orderLineItems, orderedTime);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.of(this, orderLineItems);
    }

    private void validateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        validateOrderedDateTime(orderedTime);
        validateOrderTableIsEmpty(orderTable);
        validateOrderLineItemIsEmpty(orderLineItems);
    }

    private void validateOrderedDateTime(LocalDateTime orderedTime) {
        if (orderedTime == null) {
            throw new IllegalArgumentException(EMPTY_ORDERED_DATE_ERROR_MESSAGE);
        }
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE_ERROR_MESSAGE);
        }
    }

    private void validateOrderLineItemIsEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_LINE_ITEM_ERROR_MESSAGE);
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

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
