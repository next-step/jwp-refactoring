package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.exception.ErrorMessage;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validate(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validate(OrderTable orderTable) {
        if(Objects.isNull(orderTable)) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_REQUIRED_ORDER_TABLE.getMessage());
        }
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
        }
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_CANNOT_CHANGE_COMPLETION_ORDER.getMessage());
        }
        this.orderStatus = orderStatus;
    }

    public void checkCookingOrMeal() {
        if(orderStatus.isCooking() || orderStatus.isMeal()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_CANNOT_BE_CHANGED.getMessage());
        }
    }

    public void order(List<OrderLineItem> items) {
        items.forEach(orderLineItem -> this.orderLineItems.addOrderLineItem(this, orderLineItem));
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.values();
    }
}
