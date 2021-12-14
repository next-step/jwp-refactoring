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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "orders")
public class Orders {
    private static final String CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE = "완료된 Orders 는 상태를 바꿀 수 없습니다.";
    private static final String NOT_EXIST_ORDER_TABLE = "OrderTable 이 존재하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = OrderLineItems.createEmpty();

    protected Orders() {}

    public Orders(long id) {
        this.id = id;
    }

    private Orders(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(orderTable, orderStatus, orderedTime);
        this.id = id;
    }

    private Orders(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Orders from(long id) {
        return new Orders(id);
    }

    public static Orders from(OrderTable orderTable) {
        validateExistOrderTable(orderTable);
        return new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());
    }

    public static Orders of(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Orders(id, orderTable, orderStatus, orderedTime);
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

    public OrderTable getOrderTable() {
        return orderTable;
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
        this.orderLineItems.addAll(orderLineItems);
        orderLineItems.forEach(orderLineItem -> orderLineItem.assignOrders(this));
    }

    private void validateChangeOrderStatus() {
        if (isCompletion()) {
            throw new IllegalArgumentException(CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE);
        }
    }

    private static void validateExistOrderTable(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new IllegalArgumentException(NOT_EXIST_ORDER_TABLE);
        }
    }
}
