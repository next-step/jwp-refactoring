package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(Long orderTableId) {
        this(null, orderTableId, null, null, new OrderLineItems());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        this(id, orderTableId, orderStatus, null, new OrderLineItems());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, new OrderLineItems());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(OrderRequest request) {
        Order order = new Order(request.getOrderTableId());
        order.addOrderLineItems(request.getOrderLineItems());
        return order;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatusName() {
        if (orderStatus == null) {
            return "";
        }
        return orderStatus.name();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        orderLineItems.forEach(o -> this.orderLineItems.add(OrderLineItem.of(o, this)));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isOrderStatusCompletion()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    private boolean isOrderStatusCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }
}
