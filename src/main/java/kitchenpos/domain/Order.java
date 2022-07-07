package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(Long id, Long orderTableId) {
        this.id = id;
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId) {
        this(null, orderTableId);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        for (OrderLineItem orderLineItem : orderLineItems) {
            add(orderLineItem);
        }
    }

    private void add(OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
        orderLineItem.bindTo(this);
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문내역 목록이 비어있습니다.");
        }
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
        return orderLineItems;
    }

}
