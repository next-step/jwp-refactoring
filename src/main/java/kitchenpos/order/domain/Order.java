package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.common.Messages.ORDER_STATUS_CHANGE_CANNOT_COMPLETION;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(orderTable, orderLineItems);
        this.id = id;
        this.orderStatus = orderStatus;
    }

    private Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void addOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItems.getOrderLineItems().forEach(orderLineItem -> {
            orderLineItem.setOrder(this);
            this.orderLineItems.add(orderLineItem);
        });
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(ORDER_STATUS_CHANGE_CANNOT_COMPLETION);
        }

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
        return orderLineItems.getOrderLineItems();
    }
}
