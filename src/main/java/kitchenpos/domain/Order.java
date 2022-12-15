package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @JoinColumn(name = "order_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems;

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this(orderTable, orderStatus, orderedTime, orderLineItems);
        this.id = id;
    }

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
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
                order.orderStatus) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTable, orderStatus, orderLineItems);
    }

    public void checkEmptyItems() {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 아이템이 포함되어야 합니다");
        }
    }

    public List<Long> menuIds() {
        return this.orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public void updateItemOrder() {
        this.checkEmptyItems();
        this.orderLineItems.forEach(it -> it.updateOrder(this));
    }

    public void checkValidOrderTable() {
        orderTable.checkNullId();
        orderTable.checkEmptyTable();
    }
}
