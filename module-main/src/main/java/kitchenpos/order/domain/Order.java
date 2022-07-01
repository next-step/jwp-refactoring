package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

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

    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = OrderLineItems.of(this, orderLineItems);
    }

    private Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                  List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.of(orderLineItems);
    }

    public static Order of(Long id) {
        return new Order(id);
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus,
                           List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void receive(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어 있습니다");
        }
        this.orderTableId = orderTable.getId();
        changeOrderStatus(OrderStatus.COOKING);
        this.orderedTime = LocalDateTime.now();
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }
}