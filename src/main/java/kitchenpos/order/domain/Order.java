package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.table.domain.OrderTable;

@Entity(name = "orders")
public class Order extends BaseEntity {
    private static final Integer MIN_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        initOrderStatus(orderStatus);
        setOrderTable(orderTable);
        addOrderLineItems(orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderLineItems);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateAddOrderLineItem(orderLineItems);

        for (OrderLineItem orderLineItem: orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    public boolean isOnGoing() {
        return OrderStatus.COOKING.equals(orderStatus) || OrderStatus.MEAL.equals(orderStatus);
    }

    public void removeOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.remove(orderLineItem);
    }

    public void setOrderTable(OrderTable orderTable) {
        validateOrderTable(orderTable);
        if (this.orderTable != null) {
            this.orderTable.removeOrder(this);
        }
        this.orderTable = orderTable;
        orderTable.addOrder(this);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        validateUpdateOrderStatus();
        this.orderStatus = orderStatus;
    }

    protected void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);

        if (!orderLineItem.equalsOrder(this)) {
            orderLineItem.setOrder(this);
        }
    }

    private void initOrderStatus(OrderStatus orderStatus) {
        if (Objects.isNull(orderStatus)) {
            orderStatus = OrderStatus.COOKING;
        }
        this.orderStatus = orderStatus;
    }

    private void validateUpdateOrderStatus() {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new CannotUpdatedException("계산완료된 주문은 변경할 수 없습니다.");
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new InvalidArgumentException("테이블은 필수입니다.");
        }

        if (orderTable.isEmpty()) {
            throw new InvalidArgumentException("빈 테이블은 주문을 할 수 없습니다.");
        }
    }

    private void validateAddOrderLineItem(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < MIN_SIZE) {
            throw new InvalidArgumentException("메뉴는 하나 이상 선택해야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

}
