package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.table.domain.OrderTable;

@Entity(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        initOrderStatus(orderStatus);
        setOrderTable(orderTable);

        this.orderLineItems = orderLineItems;
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderLineItems);
    }

    private void initOrderStatus(OrderStatus orderStatus) {
        if (Objects.isNull(orderStatus)) {
            orderStatus = OrderStatus.COOKING;
        }
        this.orderStatus = orderStatus;
    }

    public void setOrderTable(OrderTable orderTable) {
        validateOrderTable(orderTable);
        if (this.orderTable != null) {
            this.orderTable.removeOrder(this);
        }
        this.orderTable = orderTable;
        orderTable.addOrder(this);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new InvalidArgumentException("테이블은 필수입니다.");
        }

        if (orderTable.isEmpty()) {
            throw new InvalidArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }




    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void setOrderTableId(final Long orderTableId) {
//        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public void setOrderStatusorg(final String orderStatus) {
//        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public boolean isOnGoing() {
        return OrderStatus.COOKING.equals(orderStatus) || OrderStatus.MEAL.equals(orderStatus);

    }
}
