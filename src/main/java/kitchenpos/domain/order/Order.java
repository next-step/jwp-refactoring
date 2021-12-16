package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

import org.springframework.util.CollectionUtils;

import kitchenpos.domain.table.OrderTable;

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

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(orderTableId, orderStatus, orderedTime);
        this.id = id;
    }

    private Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order from(long id) {
        return new Order(id);
    }

    public static Order createFromOrderTable(Long orderTableId) {
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Order(id, orderTableId, orderStatus, orderedTime);
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

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateIsEmptyOrderLineItems(orderLineItems);

        this.orderLineItems.addAll(orderLineItems);
        orderLineItems.forEach(orderLineItem -> orderLineItem.assignOrders(this));
    }

    private void validateChangeOrderStatus() {
        if (isCompletion()) {
            throw new IllegalArgumentException(CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE);
        }
    }

    private void validateIsEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(EMPTY_ORDER_LINE_ITEMS);
        }
    }
}
