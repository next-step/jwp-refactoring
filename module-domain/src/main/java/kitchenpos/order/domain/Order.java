package kitchenpos.order.domain;

import kitchenpos.exception.ChangeOrderStatusFailedException;
import kitchenpos.exception.EmptyOrderLineItemException;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        addOrderLineItems(orderLineItems);
    }

    public Order(final Long id, final Long orderTableId) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
    }

    public void changeOrderStatus(final String orderStatus) {
        changeOrderStatus(OrderStatus.valueOf(orderStatus));
    }

    private void changeOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new ChangeOrderStatusFailedException("완료 상태인 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateEmptyOrderLineItems(orderLineItems);
        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void validateEmptyOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemException("주문시 주문항목은 1개 이상이어야 합니다.");
        }
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addedBy(this);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatusName() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
