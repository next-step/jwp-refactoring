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
import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

    private static final String CANNOT_CHANGE_ORDER_STATUS = "완료 주문은 상태를 바꿀 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.from(orderLineItems);

        this.orderLineItems.getReadOnlyValues()
                .forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }

    private Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.from(orderLineItems);

        this.orderLineItems.getReadOnlyValues()
                .forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus);
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                           List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order from(OrderTable orderTable) {
        return new Order(orderTable.getId(), OrderStatus.COOKING);
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateChangeOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateChangeOrderStatus() {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(CANNOT_CHANGE_ORDER_STATUS);
        }
    }

    public void addAllOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = OrderLineItems.from(orderLineItems);
        this.orderLineItems.getReadOnlyValues()
                .forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }
}
