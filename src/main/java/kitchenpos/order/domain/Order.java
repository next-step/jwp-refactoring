package kitchenpos.order.domain;

import kitchenpos.common.exception.EmptyOrderTableException;
import kitchenpos.common.exception.NotEmptyOrderLineItemException;
import kitchenpos.common.exception.OrderStatusCompletedException;
import kitchenpos.common.exception.OrderStatusNotCompletedException;
import kitchenpos.common.exception.OrderStatusNotProcessingException;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
        validateOrderLineItems();
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
        validateOrderLineItems();
    }

    private void validateOrder(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> {
            this.orderLineItems.add(orderLineItem);
            orderLineItem.decideOrder(this);
        });
    }

    private void validateOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NotEmptyOrderLineItemException();
        }
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

    public void decideOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.isCompleted(orderStatus)) {
            throw new OrderStatusCompletedException();
        }

        this.orderStatus = orderStatus;
    }

    public void validateCompleted() {
        if (!OrderStatus.isCompleted(orderStatus)) {
            throw new OrderStatusNotCompletedException();
        }
    }

    public void validateNotProcessing() {
        if (!OrderStatus.isMeal(orderStatus) && !OrderStatus.isCooking(orderStatus)) {
            return;
        }

        throw new OrderStatusNotProcessingException();
    }
}
