package kitchenpos.order.domain;

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
import kitchenpos.common.domain.OrderStatus;
import kitchenpos.common.exception.InvalidOrderStatusException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    private static final int ORDER_LINE_ITEMS_MIN_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Long orderTableId;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < ORDER_LINE_ITEMS_MIN_SIZE) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void changeStatus(OrderStatus status) {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new InvalidOrderStatusException();
        }
        this.orderStatus = status;
    }

}
