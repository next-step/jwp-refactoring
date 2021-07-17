package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.table.domain.OrderTable;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems.addAll(orderLineItems);
        this.orderLineItems.updateOrder(this);
    }

    public Order(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        this.orderTable = orderTable;
        this.orderLineItems.addAll(orderLineItems.toList());
        this.orderLineItems.updateOrder(this);
        this.orderStatus = OrderStatus.COOKING;
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문할 수 없습니다.");
        }
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.toList();
    }

    public void changeStatus(final OrderStatus orderStatus) {
        if (Objects.equals(this.orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("완료된 주문은 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public boolean isNotCompleted() {
        return !Objects.equals(orderStatus, OrderStatus.COMPLETION);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Order order = (Order)o;
        return Objects.equals(id, order.id) && Objects.equals(orderTable, order.orderTable)
            && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime)
            && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
