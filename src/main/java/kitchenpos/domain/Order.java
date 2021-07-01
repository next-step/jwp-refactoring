package kitchenpos.domain;

import kitchenpos.exception.TableEmptyException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    private OrderLineItems orderLineItems;

    @Column(name = "old_order_table_id")
    private Long orderTableId;
    @Transient
    private String oldOrderStatus;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.oldOrderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderTable = orderTable;
        this.oldOrderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order create(OrderCreate orderCreate, Menus menus, OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new TableEmptyException();
        }

        if (orderCreate.getOrderLineItems().size() != menus.size()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order();

        List<OrderLineItem> orderLineItems = orderCreate.getOrderLineItems()
                .stream()
                .map(item -> new OrderLineItem(order, menus.findById(item.getMenuId()), item.getQuantity()))
                .collect(Collectors.toList());

        order.orderTable = orderTable;
        order.orderStatus = OrderStatus.COOKING;
        order.orderedTime = LocalDateTime.now();
        order.orderLineItems = new OrderLineItems(orderLineItems);

        return order;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOldOrderStatus() {
        return oldOrderStatus;
    }

    public void setOldOrderStatus(final String oldOrderStatus) {
        this.oldOrderStatus = oldOrderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.toCollection();
    }

    public boolean isFinished() {
        return this.oldOrderStatus == OrderStatus.COMPLETION.name();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }
}
