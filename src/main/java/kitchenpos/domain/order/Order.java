package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.table.OrderTable;
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

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(this, orderLineItems);
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }

    public boolean isFinished() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.toCollection();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
