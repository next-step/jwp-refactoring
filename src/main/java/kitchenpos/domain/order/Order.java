package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

    private static final String CANNOT_CHANGE_ORDER_STATUS = "완료 주문은 상태를 바꿀 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = OrderLineItems.createEmpty();

    protected Order() {}

    private Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.from(orderLineItems);

        this.orderLineItems.getReadOnlyValues()
                .forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.from(orderLineItems);

        this.orderLineItems.getReadOnlyValues()
                .forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable, orderStatus);
    }

    public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                           List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order from(OrderTable orderTable) {
        return new Order(orderTable, OrderStatus.COOKING);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return this.orderTable.getId();
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
