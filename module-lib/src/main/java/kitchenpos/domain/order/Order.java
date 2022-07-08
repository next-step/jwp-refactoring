package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.order.OrderStatusException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Column
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderTableId = orderTable.getId();
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTable.getId();
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order from(OrderTable orderTable) {
        return new Order(orderTable);
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

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void addOrderLineItem(Long menuId, long quantity) {
        orderLineItems.add(new OrderLineItem(this, menuId, quantity));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.name().equals(this.orderStatus.name())) {
            throw new OrderStatusException(OrderStatusException.COMPLETE_DOES_NOT_CHANGE_MSG);
        }

        this.orderStatus = orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
