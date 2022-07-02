package kitchenpos.order.domain;

import static kitchenpos.Exception.OrderTableAlreadyEmptyException.ORDER_TABLE_ALREADY_EMPTY_EXCEPTION;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.Exception.OrderStatusCompleteException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "orders")
@EntityListeners({AuditingEntityListener.class})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = OrderLineItems.from(orderLineItems);
    }

    public Order(OrderTable orderTable) {
        validateOrderTableEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
    }

    public void addOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItems.connectToOrder(this);
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateChangeOrderStatus();
        this.orderStatus = orderStatus;
    }

    private static void validateOrderTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw ORDER_TABLE_ALREADY_EMPTY_EXCEPTION;
        }
    }

    private void validateChangeOrderStatus() {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new OrderStatusCompleteException("완료 주문상태인 주문은 상태를 변경할 수 없습니다.");
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
