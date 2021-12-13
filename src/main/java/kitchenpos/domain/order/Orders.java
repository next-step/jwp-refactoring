package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "orders")
public class Orders {
    private static final String CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE = "완료된 Orders 는 상태를 바꿀 수 없습니다.";

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

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

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

    private Orders(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(orderTable, orderStatus, orderedTime);
        this.orderLineItems = orderLineItems;
    }

    public static Orders from(long id) {
        return new Orders(id);
    }

    public static Orders from(OrderTable orderTable) {
        return new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now());
    }

    public static Orders of(OrderTable orderTable, OrderStatus orderStatus) {
        return new Orders(orderTable, orderStatus, LocalDateTime.now(), new ArrayList<>());
    }

    public static Orders of(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        return new Orders(id, orderTable, orderStatus, orderedTime);
    }

    public static Orders of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Orders(orderTable, orderStatus, orderedTime, orderLineItems);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        orderLineItems.forEach(orderLineItem -> orderLineItem.assignOrders(this));
    }

    private void validateChangeOrderStatus() {
        if (isCompletion()) {
            throw new IllegalArgumentException(CAN_NOT_CHANGE_ORDER_STATUS_MESSAGE);
        }
    }
}
