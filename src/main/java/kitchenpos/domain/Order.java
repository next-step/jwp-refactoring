package kitchenpos.domain;

import kitchenpos.exception.OrderStatusException;
import kitchenpos.exception.OrderTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(OrderTable orderTable) {
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderTable = orderTable;
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order from(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderTableException(OrderTableException.ORDER_TABLE_IS_EMPTY_MSG);
        }
        return new Order(orderTable);
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

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void addOrderLineItem(Menu menu, long quantity) {
        orderLineItems.add(new OrderLineItem(this, menu, quantity));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.name().equals(this.orderStatus.name())) {
            throw new OrderStatusException(OrderStatusException.COMPLETE_DOES_NOT_CHANGE_MSG);
        }

        this.orderStatus = orderStatus;
    }
}
