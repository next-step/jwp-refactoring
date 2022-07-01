package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;
import kitchenpos.order.application.OrderValidator;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    protected Order() {}

    private Order(Long orderTableId, OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.addOrderLineItems(orderLineItems);
    }

    public static Order createOrder(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    private void addOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems.addAll(this, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public Set<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getValue();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public boolean isComplete() {
        return this.orderStatus.isComplete();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if (this.isComplete()) {
            throw new IllegalArgumentException("계산 완료된 주문은 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
