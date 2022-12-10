package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.order.exception.OrderExceptionCode;
import org.springframework.util.CollectionUtils;

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
            throw new IllegalArgumentException(OrderExceptionCode.REQUIRED_ORDER_TABLE.getMessage());
        }

        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderExceptionCode.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
        }
    }

    public void order(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(OrderExceptionCode.ORDER_LINE_ITEMS_CANNOT_BE_EMPTY.getMessage());
        }

        orderLineItems.forEach(orderLineItem -> addOrderLineItem(orderLineItem));
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkForChangingOrderTable() {
        if(orderStatus.isCooking() || orderStatus.isMeal()) {
            throw new IllegalArgumentException(OrderExceptionCode.CANNOT_BE_CHANGED.getMessage());
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(OrderExceptionCode.CANNOT_CHANGE_COMPLETION_ORDER.getMessage());
        }

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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
