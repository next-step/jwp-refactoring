package kitchenpos.order.domain;

import kitchenpos.common.entity.BaseTimeEntity;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.order.exception.CompleteOrderChangeStateException;

@Entity(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

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

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        assignOrderLineItems(orderLineItems);
    }

    private void assignOrderLineItems(List<OrderLineItem> inputOrderLineItems) {
        orderLineItems.assignOrderLineItems(inputOrderLineItems, this);
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

    public Long getOrderTableId() {
        return orderTableId;
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
