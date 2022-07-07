package kichenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        changeOrderLineItems(orderLineItems);
        this.orderLineItems.addOrder(this);
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = orderedTime;
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

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException("계산 완료한 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.elements();
    }

    public void changeOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public List<Long> getMenuIds() {
        return orderLineItems.getMenuIds();
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
