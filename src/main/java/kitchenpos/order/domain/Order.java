package kitchenpos.order.domain;

import kitchenpos.common.exception.InvalidParameterException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    private static final String ERROR_MESSAGE_ORDER_STATUS_IS_COMPLETION = "이미 완료된 주문입니다.";
    private static final String ERROR_MESSAGE_ORDER_STATUS_IS_COOKING = "주문 상태가 조리 중 입니다.";
    private static final String ERROR_MESSAGE_ORDER_STATUS_IS_MEAL = "주문 상태가 식사 중 입니다.";
    private static final String ERROR_MESSATE_ORDER_TABLE_NOT_EXIST = "등록된 테이블이 존재하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {}

    private Order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTableId);
        OrderLineItems items = OrderLineItems.from(orderLineItems);
        items.updateOrder(this);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = items;
    }

    private void validateOrderTable(Long orderTableId) {
        if (orderTableId == null) {
            throw new InvalidParameterException(ERROR_MESSATE_ORDER_TABLE_NOT_EXIST);
        }
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderLineItems);
    }

    public static Order of(Long orderTableId, OrderLineItem... orderLineItems) {
        return new Order(null, orderTableId, Arrays.asList(orderLineItems));
    }

    private void validateCompletion() {
        if (orderStatus.isCompletion()) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_STATUS_IS_COMPLETION);
        }
    }

    public void validateCookingAndMeal() {
        if (orderStatus.isCooking()) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_STATUS_IS_COOKING);
        }

        if (orderStatus.isMeal()) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_STATUS_IS_MEAL);
        }
    }

    public void changeStatus(OrderStatus changeStatus) {
        validateCompletion();
        this.orderStatus = changeStatus;
    }

    public Long id() {
        return id;
    }

    public Long orderTableId() {
        return orderTableId;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public OrderLineItems orderLineitems() {
        return orderLineItems;
    }
}
