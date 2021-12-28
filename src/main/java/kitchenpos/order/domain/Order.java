package kitchenpos.order.domain;

import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    private static final String INVALID_ORDER_STATUS = "완료 상태의 주문은 변경할 수 없습니다.";
    private static final String INVALID_REVERSE_ORDER_STATUS = "거꾸로 주문을 변경할 수 없습니다.";
    private static final String INVALID_ORDER_TABLE = "빈 주문 테이블을 주문 등록 할 수 없습니다.";
    private static final String INVALID_ORDER_LINE = "주문 라인은 비어있을 수 없습니다.";
    private static final int zero = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
        assignOrderTable(orderTable);

        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.of(orderLineItems);
        this.orderLineItems.addOrder(this);
    }

    private void assignOrderTable(OrderTable orderTable) {
        if (!equalsOrderTable(orderTable)) {
            this.orderTable = orderTable;
            orderTable.addOrder(this);
        }
    }

    public boolean equalsOrderTable(OrderTable orderTable) {
        if (Objects.isNull(this.orderTable)) {
            return false;
        }

        return this.orderTable.equals(orderTable);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException(INVALID_ORDER_TABLE);
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new InvalidOrderException(INVALID_ORDER_LINE);
        }
    }

    public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Long getId() {
        return id;
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

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        validateReverseOrderStatus(orderStatus);
        this.orderStatus = orderStatus;
    }

    private void validateReverseOrderStatus(OrderStatus orderStatus) {
        if(this.orderStatus.compareTo(orderStatus) > zero){
            throw new InvalidOrderStatusException(INVALID_REVERSE_ORDER_STATUS);
        }
    }

    private void validateOrderStatus() {
        if (isComplete()) {
            throw new InvalidOrderStatusException(INVALID_ORDER_STATUS);
        }
    }

    public boolean isComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }
}
