package kitchenpos.order.domain;

import kitchenpos.order.exception.NotCreateOrderException;
import kitchenpos.order.exception.NotChangeOrderStatusException;
import kitchenpos.order.exception.OrderErrorCode;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

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
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.valueOf(orderStatus);
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    private Order(OrderTable orderTable, List<OrderLineItem> orderLineItemList) {
        if (orderTable.isEmpty()) {
            throw new NotCreateOrderException(orderTable.getId() + OrderErrorCode.EMPTY_ORDER_TABLE);
        }

        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = new OrderLineItems(orderLineItemList);
    }

    public Order(String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public static Order create(OrderTable orderTable, List<OrderLineItem> orderLineItemList) {
        Order order = new Order(orderTable, orderLineItemList);
        order.addOrderLineItems();
        return order;
    }

    private void addOrderLineItems() {
        this.orderLineItems.addOrderLineItems( this);
    }

    public void changeOrderStatus(String request) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new NotChangeOrderStatusException(OrderErrorCode.ORDER_COMPLETE);
        }

        this.orderStatus = OrderStatus.valueOf(request);
    }

    public static Order create(String orderStatus) {
        return new Order(orderStatus);
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

    public void createId(Long id) {
        this.id = id;
    }

}
