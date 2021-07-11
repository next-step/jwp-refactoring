package kitchenpos.domain;

import java.time.LocalDateTime;
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

import kitchenpos.exception.IllegalOperationException;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderStatus, orderLineItems);
    }

    Order(Long id, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public void proceedTo(OrderStatus orderStatus) {
        checkChangeable();
        this.orderStatus = orderStatus;
    }

    private void checkChangeable() {
        if (orderStatus.isCompleted()) {
            throw new IllegalOperationException("완결 된 주문은 상태를 변경할 수 없습니다.");
        }
    }

    public boolean inProgress() {
        return orderStatus.inProgress();
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
