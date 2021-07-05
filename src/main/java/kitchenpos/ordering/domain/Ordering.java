package kitchenpos.ordering.domain;

import kitchenpos.BaseEntity;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Ordering extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Ordering() { }

    public Ordering(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems(orderLineItems);
        setOrderIdOnOrderLineItems();
    }

    public Ordering(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        setOrderIdOnOrderLineItems();
    }

    public Ordering(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        setOrderIdOnOrderLineItems();
    }

    private void setOrderIdOnOrderLineItems() {
        if (Objects.isNull(orderLineItems) || orderLineItems.isNull()) {
            throw new IllegalArgumentException("테이블이 비어있으면 주문 할 수 없습니다.");
        }
        orderLineItems.setOrderIdOnOrderLineItems(this);
    }

    public void isFrom(OrderTable orderTable) {
        this.orderTableId = orderTable.getId();
    }

    public void validateOrderLineItemsSize(long savedMenuIdsSize) {
        if (orderLineItems.isEmpty() ||
                orderLineItems.size() != savedMenuIdsSize) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> menuIds() {
        return orderLineItems.menuIds();
    }

    public void changeOrderStatusTo(OrderStatus orderStatus) {
        checkIfAlreadyCompleted();

        this.orderStatus = orderStatus;
    }

    private void checkIfAlreadyCompleted() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ordering order = (Ordering) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId, order.orderTableId) && Objects.equals(orderStatus, order.orderStatus) && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

}
