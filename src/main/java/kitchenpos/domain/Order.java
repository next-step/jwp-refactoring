package kitchenpos.domain;

import kitchenpos.exception.CannotProgressException;
import kitchenpos.exception.NoOrderLineItemException;
import kitchenpos.exception.NoOrderTableException;

import javax.naming.CannotProceedException;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime localDateTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = localDateTime;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        validateOrderLineItemsExists();
        validateOrderTable();
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable, orderStatus, LocalDateTime.now());
    }

    private void validateOrderTable() {
        if (orderTable.isEmpty()) {
            throw new NoOrderTableException();
        }
    }

    private void validateOrderLineItemsExists() {
        if (this.orderLineItems.empty()) {
            throw new NoOrderLineItemException();
        }
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

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.getOrderStatus())) {
            throw new CannotProgressException();
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }
}
