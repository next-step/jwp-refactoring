package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderLineItemNotFoundException;
import kitchenpos.order.exception.OrderStatusUpdateException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.InvalidTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @Column(nullable = false)
    private final LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        addOrderLineItems(orderLineItems);
        this.orderTable = orderTable.addOrder(this);
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.add(this, orderLineItems);
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new InvalidTableException();
        }
        if (orderLineItems.isEmpty()) {
            throw new OrderLineItemNotFoundException();
        }
    }

    public void changeStatus(OrderStatus changeStatus) {
        if (orderStatus.isCompletion()) {
            throw new OrderStatusUpdateException();
        }
        this.orderStatus = changeStatus;
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
        return orderLineItems.value();
    }

}
