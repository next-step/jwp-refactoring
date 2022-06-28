package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(Long orderTableId) {
        this(null, orderTableId, null, null, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        this(id, orderTableId, orderStatus, null, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatusName() {
        if (orderStatus == null) {
            return "";
        }
        return orderStatus.name();
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

    public void addOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        orderLineItems.forEach(orderLineItemResponse ->
                addOrderLineItem(orderLineItemResponse.toOrderLineItem(this)));
    }

    private void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isOrderStatusCompletion()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    private boolean isOrderStatusCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public void changeOrderedTimeToCurrentTime() {
        this.orderedTime = LocalDateTime.now();
    }
}
