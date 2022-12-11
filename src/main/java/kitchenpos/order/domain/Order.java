package kitchenpos.order.domain;

import kitchenpos.exception.OrderError;
import kitchenpos.exception.OrderLineItemError;
import kitchenpos.exception.OrderTableError;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {

    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validate(orderTable);

        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validate(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new IllegalArgumentException(OrderTableError.NOT_FOUND);
        }
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(OrderTableError.CANNOT_EMPTY);
        }
    }

    public void order(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(OrderLineItemError.CANNOT_EMPTY);
        }

        orderLineItems.forEach(this::addOrderLineItem);
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkOngoingOrderTable() {
        if (orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL) {
            throw new IllegalArgumentException(OrderError.CANNOT_CHANGE);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException(OrderError.CANNOT_CHANGE_STATUS);
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
