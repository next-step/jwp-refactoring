package kitchenpos.order.domain;

import kitchenpos.common.domain.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long id, Long orderTableId) {
        this.id = id;
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;
        addOrderLineItems(orderLineItems);
    }

    public Order(Long orderTableId) {
        this(null, orderTableId);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(this, orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompleted()) {
            throw new IllegalArgumentException("주문의 상태가 이미 완료됐습니다.");
        }
        this.orderStatus = orderStatus;
    }

    private boolean isCompleted() {
        return this.orderStatus == OrderStatus.COMPLETION;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

}
