package kitchenpos.order.domain;

import kitchenpos.orderTable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, LocalDateTime orderedTime) {
        validateOrderTable(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderedTime = orderedTime;
    }

    public Order(OrderTable orderTable, LocalDateTime orderedTime) {
        validateOrderTable(orderTable);
        this.orderTable = orderTable;
        this.orderedTime = orderedTime;
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void registerOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;

        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.size() < 1) {
            throw new IllegalArgumentException();
        }
        if(orderLineItems.size() !=
                orderLineItems.stream()
                        .map(orderLineItem -> orderLineItem.getMenu())
                        .distinct()
                        .count()){
            throw new IllegalArgumentException();
        }
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCooking() {
        return orderStatus == OrderStatus.COOKING;
    }

    public boolean isEating() {
        return orderStatus == OrderStatus.MEAL;
    }

    public boolean isComplete() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
