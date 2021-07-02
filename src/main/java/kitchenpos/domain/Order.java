package kitchenpos.domain;

import static java.util.Objects.*;
import static kitchenpos.domain.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;

    public Order() {}

    private Order(OrderTable orderTable, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = COOKING;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    public static Order create(List<OrderLineItem> orderLineItems, OrderTable orderTable, LocalDateTime orderedTime) {
        if (isNull(orderLineItems) || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 없습니다.");
        }
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈테이블에서 주문할 수 없습니다.");
        }
        return new Order(orderTable, orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void complete() {
        validateNotCompleted();
        this.orderStatus = COMPLETION;
    }

    public void startMeal() {
        validateNotCompleted();
        this.orderStatus = MEAL;
    }

    private void validateNotCompleted() {
        if (isComplete()) {
            throw new IllegalArgumentException("계산 완료 주문은 상태를 변경할 수 없습니다.");
        }
    }

    boolean isComplete() {
        return this.orderStatus == COMPLETION;
    }
}
