package kitchenpos.order.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.order.domain.value.OrderLineItems;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.exception.OrderStatusCompletionException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    private String orderStatus = OrderStatus.COOKING.name();

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id, Long orderTableId, String orderStatus,
        OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        orderLineItems.toOrder(this);
    }

    private Order(Long orderTableId, OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        orderLineItems.toOrder(this);
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void changeOrderStatus(String orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new OrderStatusCompletionException();
        }
    }

    public List<OrderLineItem> getOrderLineItemList() {
        return orderLineItems.getValue();
    }
}
