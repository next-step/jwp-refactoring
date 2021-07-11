package kitchenpos.order.domain;

import kitchenpos.order.domain.exception.UnUseOrderException;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
        orderLineItems.registerAll(this);
    }

    public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, OrderLineItems.of(orderLineItems));
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(null, orderTable, orderStatus, orderedTime, OrderLineItems.of(new ArrayList<>()));
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        orderTable.validateOrderable();
        return new Order(orderTable, OrderLineItems.of(orderLineItems));
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
        return orderLineItems.getUnmodifiableList();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderable();
        this.orderStatus = orderStatus;
    }

    private void validateOrderable() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION)) {
            throw new UnUseOrderException();
        }
    }
}
