package kitchenpos.domain.order.domain;

import kitchenpos.domain.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(orderTable, orderStatus, orderedTime, null);
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(orderTable, orderStatus, null, orderLineItems);
    }

    public Order(OrderStatus orderStatus) {
        this(null, orderStatus, null, null);
    }

    protected Order() {
    }

    public static Order create(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now());
        order.addOrderLineItems(orderLineItems);
        return order;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.addMenu(this));
        this.orderLineItems = orderLineItems;
    }

    public void checkCompleteOrder() {
        if (isComplete()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isComplete() {
        return orderStatus.isComplete();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
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
