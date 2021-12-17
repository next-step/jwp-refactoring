package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    public static final String MESSAGE_VALIDATE_ORDER_STATUS = "주문 상태가 계산 완료여야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

    public void changeOrderTable(Long orderTableId, OrderValidator orderValidator) {
        orderValidator.validate(orderTableId);
        this.orderTableId = orderTableId;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    public boolean isChangable() {
        return orderStatus.isCompletion();
    }

    void addToOrderLineItems(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    private void validateOrderStatus() {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_ORDER_STATUS);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime);
    }
}
