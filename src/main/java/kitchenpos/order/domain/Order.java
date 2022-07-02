package kitchenpos.order.domain;

import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.order.exception.IllegalOrderLineItemException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    @CreatedDate
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public static final String ERROR_ORDER_TABLE_EMPTY = "주문테이블은 비어있을 수 없습니다.";
    public static final String ERROR_ORDER_INVALID_STATUS = "주문의 상태는 %s일 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_TOO_SMALL = "주문항목 개수는 %d 미만일 수 없습니다.";
    public static final String ERROR_ORDER_LINE_ITEM_DUPLICATED = "주문항목은 중복이 불가합니다.";
    private static final int MINIMUM_ORDER_LINE_ITEM_NUMBER = 1;

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = id;
        validateOrderTableNotEmpty(orderTable);
        this.orderTable = orderTable;
        registerOrderLineItems(orderLineItems);
    }

    private Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTableNotEmpty(orderTable);
        this.orderTable = orderTable;
        registerOrderLineItems(orderLineItems);
    }

    public static Order of(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public static Order of(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_EMPTY);
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
                    String.format(ERROR_ORDER_LINE_ITEM_TOO_SMALL, MINIMUM_ORDER_LINE_ITEM_NUMBER)
            );
        }
    }

    private void validateOrderLineItemsDuplicated(List<OrderLineItem> orderLineItems) {
        if(orderLineItems.size() !=
                orderLineItems.stream()
                        .map(orderLineItem -> orderLineItem.getMenu())
                        .distinct()
                        .count()){
            throw new IllegalOrderLineItemException(ERROR_ORDER_LINE_ITEM_DUPLICATED);
        }
    }

    public void changeStatus(OrderStatus orderStatus) {
        validateOrderStatusChangeable();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatusChangeable() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalOrderException(
                    String.format(ERROR_ORDER_INVALID_STATUS, OrderStatus.COMPLETION)
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
