package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(Long orderTableId) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = now();
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = now();
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

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItem.toOrder(this);
    }

    public boolean isEmptyItem() {
        return orderLineItems.isEmpty();
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
