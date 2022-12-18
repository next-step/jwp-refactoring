package kitchenpos.order.domain;


import kitchenpos.exception.BadRequestException;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.utils.Message.EMPTY_ORDER_TABLE;
import static kitchenpos.utils.Message.INVALID_CHANGE_ORDER_STATUS;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = OrderLineItems.from(orderLineItems);
        checkOrderTableIsNotEmpty(orderTable);
        orderTable.addOrder(this);
        this.orderLineItems.setup(this);
    }

    public static Order of(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }

    public static Order of(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }


    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus())) {
            throw new BadRequestException(INVALID_CHANGE_ORDER_STATUS);
        }
        this.orderStatus = orderStatus;
    }

    private static void checkOrderTableIsNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new BadRequestException(EMPTY_ORDER_TABLE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
