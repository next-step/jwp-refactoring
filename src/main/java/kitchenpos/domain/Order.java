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
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime localDateTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = localDateTime;
        this.orderLineItems = new OrderLineItems();
        validateOrderTable();
    }

    public Order() {

    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable, orderStatus, LocalDateTime.now());
    }

    private void validateOrderTable() {
        if (orderTable == null) {
            throw new NoOrderTableException();
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void makeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.getOrderStatus())) {
            throw new CannotProgressException();
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }
}
