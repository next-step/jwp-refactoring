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
import kitchenpos.common.constant.ErrorCode;

@Table(name = "ORDERS")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {}

    private Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        orderLineItems.setUpOrder(this);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, OrderLineItems.from(orderLineItems));
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(null, orderTableId, orderLineItems);
    }

    private void validateOrderTable(Long orderTableId) {
        if(orderTableId == null) {
            throw new IllegalArgumentException(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatusCompletion();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusCompletion() {
        if(Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ErrorCode.이미_완료된_주문.getErrorMessage());
        }
    }

    public void validateIfNotCompletionOrder() {
        if(!Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ErrorCode.완료되지_않은_주문.getErrorMessage());
        }
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
