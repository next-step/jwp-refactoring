package kitchenpos.order.domain;

import static java.util.Objects.*;
import static kitchenpos.order.domain.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    private OrderTableId orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {}

    public Order(OrderTableId tableId, OrderLineItems orderLineItems, LocalDateTime orderedTime) {
        validateNonNull(tableId, orderLineItems, orderedTime);
        this.orderTableId = tableId;
        this.orderStatus = COOKING;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.toOrder(this);
    }

    public Long getId() {
        return id;
    }

    public OrderTableId getOrderTableId() {
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

    public void complete() {
        validateNotCompleted();
        this.orderStatus = COMPLETION;
    }

    public void startMeal() {
        validateNotCompleted();
        this.orderStatus = MEAL;
    }

    public void startCooking() {
        validateNotCompleted();
        this.orderStatus = COOKING;
    }

    public boolean isComplete() {
        return this.orderStatus == COMPLETION;
    }

    public boolean isCreatedFrom(OrderTableId orderTableId) {
        return this.orderTableId.equals(orderTableId);
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (orderStatus == COOKING) {
            startCooking();
            return;
        }
        if (orderStatus == MEAL) {
            startMeal();
            return;
        }
        complete();
    }

    private void validateNotCompleted() {
        if (isComplete()) {
            throw new IllegalArgumentException("계산 완료 주문은 상태를 변경할 수 없습니다.");
        }
    }

    private void validateNonNull(OrderTableId orderTableId, OrderLineItems orderLineItems, LocalDateTime orderedTime) {
        if (isNull(orderTableId) || isNull(orderLineItems) || isNull(orderedTime)) {
            throw new IllegalArgumentException("주문 필수 정보가 없습니다.");
        }
    }
}
