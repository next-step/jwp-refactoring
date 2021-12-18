package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    private static final String CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE = "완료된 Order 는 상태를 바꿀 수 없습니다.";

    private static final String EMPTY_ORDER_LINE_ITEMS = "OrderLineItems 가 비어있습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = OrderLineItems.createEmpty();

    protected Order() {}

    public Order(long id) {
        this.id = id;
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(orderTableId, orderStatus, orderLineItems);
        this.id = id;
    }

    private Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = OrderLineItems.from(orderLineItems);

        this.orderLineItems.getValues().forEach(orderLineItem -> orderLineItem.assignOrders(this));
    }

    public static Order from(long id) {
        return new Order(id);
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public boolean isCompletion() {
        return orderStatus.isCompletion();
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateChangeOrderStatus();
        this.orderStatus = orderStatus;
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

    private void validateChangeOrderStatus() {
        if (isCompletion()) {
            throw new IllegalArgumentException(CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE);
        }
    }
}
