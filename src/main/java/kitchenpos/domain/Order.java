package kitchenpos.domain;

import kitchenpos.exception.InvalidTableException;
import kitchenpos.exception.OrderLineItemNotFoundException;
import kitchenpos.exception.OrderStatusUpdateException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @Column(nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        addOrderLineItems(orderLineItems);
        //this.orderTable = orderTable.addOrder(this);
        this.orderTable = orderTable;
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(it -> this.orderLineItems.add(it.include(this)));
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new InvalidTableException();
        }

        if (orderLineItems.isEmpty()) {
            throw new OrderLineItemNotFoundException();
        }
    }

    public boolean isCompleted() {
        return orderStatus.isCompletion();
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

    public void changeStatus(OrderStatus changeStatus) {
        if( orderStatus.isCompletion()) {
            throw new OrderStatusUpdateException();
        }
        this.orderStatus = changeStatus;
    }
}
