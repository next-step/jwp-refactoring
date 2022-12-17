package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.order.application.OrderService.COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE;
import static kitchenpos.order.application.OrderService.ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.table.application.TableService.ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE;


@Entity
@Table(name = "orders")
public class Order {
    public static final String ORDER_TABLE_NULL_EXCEPTION_MESSAGE = "주문 테이블이 없습니다.";
    public static final String COMPLETION_CHANGE_EXCEPTION_MESSAGE = "완료일 경우 변경할 수 없습니다.";
    public static final String ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "주문 테이블은 비어있을 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDERLINEITEMS_EMPTY_EXCEPTION_MESSAGE);
        }
        if (Objects.isNull(orderTable)) {
            throw new IllegalArgumentException(ORDER_TABLE_NULL_EXCEPTION_MESSAGE);
        }
        this.orderTable = orderTable;
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
        }
        this.orderLineItems.addAll(orderLineItems);
//        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public Order(long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public void meal() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException(COMPLETION_CHANGE_EXCEPTION_MESSAGE);
        }
        this.orderStatus = OrderStatus.MEAL;
    }

    public void complete() {
        this.orderStatus = OrderStatus.COMPLETION;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems.getOrderLineItems();
    }

    public OrderTable getOrderTable() {
        return this.orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException(COMPLETION_NOT_CHANGE_EXCEPTION_MESSAGE);
        }
        this.orderStatus = orderStatus;
    }

    public void emptyTable() {
        if (this.orderTable == null) {
            throw new EntityNotFoundException("주문테이블이 존재하지 않습니다.");
        }
        if (this.orderStatus.equals(OrderStatus.COOKING) || this.orderStatus.equals(OrderStatus.MEAL)) {
            throw new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
        }
        this.orderTable.empty();
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }
}
