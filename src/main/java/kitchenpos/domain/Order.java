package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {}

    private Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.toOrder(this);
    }

    public static Order create(List<OrderLineItem> orderLineItems, OrderTable orderTable, LocalDateTime orderedTime) {
        return create(OrderLineItems.of(orderLineItems), orderTable, orderedTime);
    }

    static Order create(OrderLineItems orderLineItems, OrderTable orderTable, LocalDateTime orderedTime) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈테이블에서 주문할 수 없습니다.");
        }
        return new Order(orderTable, COOKING, orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
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
