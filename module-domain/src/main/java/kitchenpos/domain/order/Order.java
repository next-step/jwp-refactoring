package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Menus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public static Order createOrder(OrderCreate orderCreate, Menus menus) {
        return createOrder(null, orderCreate, menus);
    }

    public static Order createOrder(Long orderId, OrderCreate orderCreate, Menus menus) {
        if (orderCreate.sizeOfOrderLineItems() != menus.size()) {
            throw new IllegalArgumentException();
        }

        List<OrderLineItem> orderLineItems = parseOrderLineItemBy(orderCreate, menus);

        return new Order(orderId, orderCreate.getOrderTableId(), orderCreate.getOrderStatus(), LocalDateTime.now(), orderLineItems);
    }

    private static List<OrderLineItem> parseOrderLineItemBy(OrderCreate orderCreate, Menus menus) {
        List<OrderLineItem> orderLineItems = orderCreate.getOrderLineItems()
                .stream()
                .map(item -> {
                    Menu menu = menus.findById(item.getMenuId());
                    return new OrderLineItem(null, menu.getId(), menu.getName(), menu.getPrice(), item.getQuantity());
                })
                .collect(Collectors.toList());
        return orderLineItems;
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
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

    public Long getOrderTableId() {
        return orderTableId;
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
