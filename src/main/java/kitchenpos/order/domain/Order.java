package kitchenpos.order.domain;

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

    public Order(OrderTableId orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems,
        LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
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

    public boolean isFrom(OrderTableId orderTableId) {
        return this.orderTableId.equals(orderTableId);
    }

    private void validateNotCompleted() {
        if (isComplete()) {
            throw new IllegalArgumentException("계산 완료 주문은 상태를 변경할 수 없습니다.");
        }
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
}
