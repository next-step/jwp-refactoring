package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.aspectj.weaver.ast.Or;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItemBag orderLineItemBag;

    private Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
            OrderLineItemBag orderLineItemBag) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemBag = orderLineItemBag;
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
            OrderLineItemBag orderLineItemBag) {
        checkValidOrderTable(orderTable);
        return new Order(orderTable, orderStatus, orderedTime, orderLineItemBag);
    }

    protected Order() {
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

    public OrderLineItemBag getOrderLineItemBag() {
        return orderLineItemBag;
    }

    public boolean isStatus(OrderStatus status) {
        return this.orderStatus.equals(status);
    }

    public void changeStatus(OrderStatus orderStatus) {
        validChangeableStatus();
        this.orderStatus = orderStatus;
    }

    private void validChangeableStatus() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("계산 완료 된 주문은 주문 상태를 변경할 수 없습니다");
        }
    }

    public List<Long> menuIds() {
        return this.orderLineItemBag.menuIds();
    }

    public void updateItemOrder() {
        this.orderLineItemBag.updateItemOrder(this);
    }

    private static void checkValidOrderTable(OrderTable orderTable) {
        orderTable.checkNullId();
        orderTable.checkEmptyTable();
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
        return Objects.equals(orderTable, order.orderTable) && Objects.equals(orderStatus,
                order.orderStatus) && Objects.equals(orderLineItemBag, order.orderLineItemBag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTable, orderStatus, orderLineItemBag);
    }
}
