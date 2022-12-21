package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(Long id,
                 OrderTable orderTable,
                 OrderStatus orderStatus,
                 LocalDateTime orderedTime) {
        this(orderTable, orderStatus, orderedTime);
        this.id = id;
    }

    public Order(OrderTable orderTable,
                 OrderStatus orderStatus,
                 LocalDateTime orderedTime) {
        validate(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(OrderTable orderTable,
                OrderStatus orderStatus,
                LocalDateTime orderedTime,
                OrderLineItems orderLineItems) {
        this(orderTable, orderStatus, orderedTime);
        this.orderLineItems = orderLineItems;
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
        }
    }

    public void validateOrderStatusShouldComplete() {
        if (!isCompletion()) {
            throw new IllegalArgumentException(ErrorEnum.NOT_PAYMENT_ORDER.message());
        }
    }

    public boolean isCompletion() {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
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

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public void setOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
        orderLineItems.setOrder(this);
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
