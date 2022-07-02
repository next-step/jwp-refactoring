package kitchenpos.order.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.exception.IllegalOrderException;
import kitchenpos.exception.IllegalOrderLineItemException;
import kitchenpos.exception.IllegalOrderTableException;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    private static final int MINIMUM_ORDER_LINE_ITEM_NUMBER = 1;

    protected Order() {
    }

    private Order(OrderTable orderTable, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderTableNotEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderedTime = orderedTime;
        registerOrderLineItems(orderLineItems);
    }

    public static Order of(OrderTable orderTable, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderedTime, orderLineItems);
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalOrderTableException(ErrorMessage.ERROR_ORDER_TABLE_EMPTY);
        }
    }

    private void registerOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrder(this));
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsSize(orderLineItems);
        validateOrderLineItemsDuplicated(orderLineItems);
    }

    private void validateOrderLineItemsSize(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.size() < MINIMUM_ORDER_LINE_ITEM_NUMBER) {
            throw new IllegalOrderLineItemException(
                    String.format(ErrorMessage.ERROR_ORDER_LINE_ITEM_TOO_SMALL, MINIMUM_ORDER_LINE_ITEM_NUMBER)
            );
        }
    }

    private void validateOrderLineItemsDuplicated(List<OrderLineItem> orderLineItems) {
        if(orderLineItems.size() !=
                orderLineItems.stream()
                        .map(orderLineItem -> orderLineItem.getMenu())
                        .distinct()
                        .count()){
            throw new IllegalOrderLineItemException(ErrorMessage.ERROR_ORDER_LINE_ITEM_DUPLICATED);
        }
    }

    public void changeStatus(OrderStatus orderStatus) {
        validateOrderStatusChangeable();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusChangeable() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalOrderException(
                    String.format(ErrorMessage.ERROR_ORDER_INVALID_STATUS, OrderStatus.COMPLETION)
            );
        }
    }

    public boolean isCooking() {
        return OrderStatus.COOKING.equals(orderStatus);
    }

    public boolean isEating() {
        return OrderStatus.MEAL.equals(orderStatus);
    }

    public boolean isComplete() {
        return OrderStatus.COMPLETION.equals(orderStatus);
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
}
