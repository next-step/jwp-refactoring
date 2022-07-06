package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.TableGroup;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends AbstractAggregateRoot<Order> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
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

    public Order(Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, null, orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        orderLineItems.setOrder(this);
    }

    public static Order of(OrderRequest orderRequest) {
        return new Order(
                orderRequest.getOrderTableId(),
                OrderStatus.COOKING,
                toOrderLineItems(orderRequest.getOrderLineItems()));
    }

    private static OrderLineItems toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(OrderLineItem::of)
                .collect(Collectors.toList());

        return new OrderLineItems(orderLineItems);
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
            throw new IllegalArgumentException("주문상태가 Completion 입니다.");
        }
        this.orderStatus = orderStatus;
    }

    private boolean isOrderStatusCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

}
