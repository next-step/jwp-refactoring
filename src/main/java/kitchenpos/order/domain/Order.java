package kitchenpos.order.domain;

import kitchenpos.order.domain.exception.CannotUngroupException;
import kitchenpos.order.domain.exception.CannotChangeOrderStatusException;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
        orderLineItems.registerAll(this);
        orderTable.addOrder(this);
    }

    public static Order createWithMapping(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        orderTable.validateOrderable();
        return new Order(orderTable, orderStatus, OrderLineItems.of(orderLineItems));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderable();
        this.orderStatus = orderStatus;
    }

    private void validateOrderable() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION)) {
            throw new CannotChangeOrderStatusException();
        }
    }

    public void validateNotCompletionStatus() {
        if (Objects.equals(orderStatus, OrderStatus.COOKING) ||
                Objects.equals(orderStatus, OrderStatus.MEAL)) {
            throw new CannotUngroupException();
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getUnmodifiableList();
    }
}
