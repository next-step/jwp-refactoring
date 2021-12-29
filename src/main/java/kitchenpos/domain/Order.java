package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyOrderTableException;
import kitchenpos.common.exceptions.EmptyOrderTableStatusException;
import kitchenpos.common.exceptions.OrderStatusCompletedException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Embedded
    private OrderLineItems orderLineItems = OrderLineItems.empty();

    public Order() {
    }

    private Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final OrderTable orderTable, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        return of(null, orderTable, orderStatus, orderLineItems);
    }

    public static Order of(final Long id, final OrderTable orderTable, final OrderStatus orderStatus) {
        return of(id, orderTable, orderStatus, OrderLineItems.empty());
    }

    public static Order of(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        validate(orderTable, orderStatus);
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }

    public static Order of(final OrderTable orderTable, final OrderStatus orderStatus) {
        return new Order(null, orderTable, orderStatus, OrderLineItems.empty());
    }

    private static void validate(final OrderTable orderTable, OrderStatus orderStatus) {
        if (Objects.isNull(orderTable)) {
            throw new EmptyOrderTableException();
        }
        if (Objects.isNull(orderStatus)) {
            throw new EmptyOrderTableStatusException();
        }
    }

    public static Order from(final OrderTable orderTable) {
        return of(null, orderTable, OrderStatus.COOKING, OrderLineItems.empty());
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItemList) {
        orderLineItemList.forEach(this::addOrderLineItem);
    }

    private void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItem.decideOrder(this);
        this.orderLineItems.add(orderLineItem);
    }

    public void updateStatus(final OrderStatus updateStatusName) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new OrderStatusCompletedException();
        }
        this.orderStatus = updateStatusName;
    }

    public Long getId() {
        return this.id;
    }

    public OrderTable getOrderTable() {
        return this.orderTable;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public OrderLineItems getOrderLineItems() {
        return this.orderLineItems;
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
