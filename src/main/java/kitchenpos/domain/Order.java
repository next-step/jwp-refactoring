package kitchenpos.domain;

import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.InvalidOrderStatusException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Order {
    private static final String INVALID_ORDER_STATUS = "완료 상태의 주문은 변경할 수 없습니다.";
    private static final String INVALID_ORDER_TABLE = "빈 주문 테이블을 주문 등록 할 수 없습니다.";
    private static final String INVALID_ORDER_LINE = "주문 라인은 비어있을 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new InvalidOrderException(INVALID_ORDER_TABLE);
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if(orderLineItems.isEmpty()) {
            throw new InvalidOrderException(INVALID_ORDER_LINE);
        }
    }

    public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, LocalDateTime.now(),orderLineItems );
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void setOrderTableId(final Long orderTableId) {
        orderTable.setId(orderTableId);
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if(orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new InvalidOrderStatusException(INVALID_ORDER_STATUS);
        }
    }
}
