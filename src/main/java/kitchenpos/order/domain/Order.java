package kitchenpos.order.domain;

import java.time.LocalDateTime;
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
import javax.persistence.Table;
import kitchenpos.common.exception.Message;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public static Order createCook(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, orderLineItems);
    }

    private Order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        validIsNotNull(orderTableId);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        association(orderLineItems);
    }

    private void validIsNotNull(Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new IllegalArgumentException(Message.ORDER_TABLE_IS_NOT_NULL.getMessage());
        }
    }

    protected Order() {
    }

    public void changeOrderStatus(final String changeOrderStatus) {
        if (OrderStatus.isEqualsCompletion(getOrderStatus())) {
            throw new IllegalArgumentException(Message.ORDER_STATUS_IS_NOT_COMPLETION.getMessage());
        }
        this.orderStatus = OrderStatus.valueOf(changeOrderStatus);
    }

    private void association(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.association(orderLineItems, this);
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
        return orderLineItems.getList();
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
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
