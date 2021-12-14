package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderTable(OrderTable orderTable) {
        validateOrderTable(orderTable);
        orderTable.getOrders().add(this);
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    public boolean isChangable() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }

    void addToOrderLineItems(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new IllegalArgumentException();
        }
    }
}
