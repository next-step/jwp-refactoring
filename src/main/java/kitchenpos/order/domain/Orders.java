package kitchenpos.order.domain;

import kitchenpos.exception.OrderException;
import kitchenpos.order.enums.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    public static final String CHECK_ORDER_STATUE_ERROR_MESSAGE = "주문 상태가 완료인 경우 주문 상태 변경이 불가능 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Orders() {
    }

    public Orders(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Orders(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTableId, orderStatus, orderedTime);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.withOrder(this);
            this.orderLineItems.addOrderLineItem(orderLineItem);
        }
    }

    public void checkOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new OrderException(CHECK_ORDER_STATUE_ERROR_MESSAGE);
        }
    }

    public boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
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
