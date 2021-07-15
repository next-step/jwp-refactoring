package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @Column(nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    Order(Long id, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderLineItems = orderLineItems;
    }

    public void place(OrderValidator validator, Long orderTableId) {
        this.orderTableId = orderTableId;
        validator.validate(this);
    }

    public void changeStatus(OrderValidator validator, OrderStatus orderStatus) {
        validator.validateChangeOrderStatus(this);
        this.orderStatus = orderStatus;
    }

    public boolean isInProgress() {
        return orderStatus.inProgress();
    }

    public boolean isCompleted() {
        return orderStatus.isCompleted();
    }

    public List<Long> getMenuIds() {
        return orderLineItems.mapList(OrderLineItem::getMenuId);
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order)o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
