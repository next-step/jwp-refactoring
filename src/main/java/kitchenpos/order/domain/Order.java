package kitchenpos.order.domain;

import kitchenpos.common.exception.EmptyOrderTableException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.common.exception.OrderStatusCompletedException;
import kitchenpos.common.exception.OrderStatusNotCompletedException;
import kitchenpos.common.exception.OrderStatusNotProcessingException;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.NONE;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.NONE;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
    }

    private void validateOrder(OrderTable orderTable) {
        orderTable = Optional.ofNullable(orderTable)
                .orElseThrow(NotFoundEntityException::new);

        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> {
            this.orderLineItems.addOrderLineItem(orderLineItem);
            orderLineItem.decideOrder(this);
        });
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
        return orderLineItems.getOrderLineItems();
    }

    public void decideOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.isCompleted(orderStatus)) {
            throw new OrderStatusCompletedException();
        }

        this.orderStatus = orderStatus;
    }

    public void validateCompletedWhenUngroup() {
        if (!OrderStatus.isCompleted(orderStatus)) {
            throw new OrderStatusNotCompletedException();
        }
    }

    public void validateNotProcessingWhenChangeEmpty() {
        if (!OrderStatus.isMeal(orderStatus) && !OrderStatus.isCooking(orderStatus)) {
            return;
        }

        throw new OrderStatusNotProcessingException();
    }
}
