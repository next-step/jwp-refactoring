package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;
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
import kitchenpos.common.entity.BaseTimeEntity;
import kitchenpos.order.exception.ClosedTableOrderException;
import kitchenpos.order.exception.CompleteOrderChangeStateException;
import kitchenpos.ordertable.domain.OrderTable;

@Entity(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    protected Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        assignOrderLineItems(orderLineItems);
        assignTable(orderTable);
    }

    private void assignOrderLineItems(List<OrderLineItem> inputOrderLineItems) {
        orderLineItems.assignOrderLineItems(inputOrderLineItems, this);
    }

    private void assignTable(OrderTable orderTable) {
        validateNotOrderClosedTable(orderTable);
        this.orderTable = orderTable;
        orderTable.addOrder(this);
    }

    private void validateNotOrderClosedTable(OrderTable orderTable) {
        if (orderTable.isOrderClose()) {
            throw new ClosedTableOrderException();
        }
    }

    public boolean isCompleteStatus() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public void changeOrderStatus(OrderStatus changeStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new CompleteOrderChangeStateException();
        }

        this.orderStatus = changeStatus;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItemList() {
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Order)) {
            return false;
        }

        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
