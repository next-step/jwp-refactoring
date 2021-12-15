package kitchenpos.domain.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(orderTableId, orderStatus, orderedTime, null);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(orderTableId, orderStatus, null, orderLineItems);
    }

    public Order(OrderStatus orderStatus) {
        this(null, orderStatus, null, null);
    }

    protected Order() {
    }

    public static Order create(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
        order.addOrderLineItems(orderLineItems);
        return order;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.addMenu(this));
        this.orderLineItems = orderLineItems;
    }

    public void validateCompleteOrder() {
        if (isComplete()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isComplete() {
        return orderStatus.isComplete();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateCompleteOrder();
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {return orderTableId;}

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
