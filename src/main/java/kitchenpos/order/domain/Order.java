package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems, OrderStatus orderStatus) {
        this(orderTable, orderLineItems);
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(orderTable);
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public boolean isMeal() {
        return this.orderStatus.equals(OrderStatus.MEAL);
    }

    public boolean isCooking() {
        return this.orderStatus.equals(OrderStatus.COOKING);
    }

    public boolean changeable() {
        return !isMeal() && !isCooking();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (orderTable != null ? !orderTable.equals(order.orderTable) : order.orderTable != null) return false;
        if (orderStatus != order.orderStatus) return false;
        if (orderedTime != null ? !orderedTime.equals(order.orderedTime) : order.orderedTime != null) return false;
        return orderLineItems != null ? orderLineItems.equals(order.orderLineItems) : order.orderLineItems == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderTable != null ? orderTable.hashCode() : 0);
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (orderedTime != null ? orderedTime.hashCode() : 0);
        result = 31 * result + (orderLineItems != null ? orderLineItems.hashCode() : 0);
        return result;
    }
}
