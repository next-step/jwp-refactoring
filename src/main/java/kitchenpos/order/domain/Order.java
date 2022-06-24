package kitchenpos.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
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
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    protected Order() {
    }

    public Order(Long id, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long id, OrderStatus orderStatus, LocalDateTime orderedTime, OrderTable orderTable) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
    }

    public void registerOrderLineItems(OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 주문 항목이 없습니다.");
        }
        this.orderLineItems.addOrderLineItems(orderLineItems, this);
    }

    public void checkPossibleUngroupingOrderStatus() {
        if (OrderStatus.MEAL.equals(orderStatus) || OrderStatus.COOKING.equals(orderStatus)) {
            throw new IllegalArgumentException("[ERROR] 주문 상태가 조리, 식사 인 경우 단체 지정 해제 할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
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

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void addOrderLineItems(OrderLineItem orderLineItem) {
        orderLineItems.addOrderLineItems(orderLineItem);
        orderLineItem.setOrder(this);
    }

    public void checkPossibleChangeEmpty() {
        if (!OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalStateException("[ERROR] 주문이 계산완료 상태가 아닙니다.");
        }
    }
}
