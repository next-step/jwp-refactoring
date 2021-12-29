package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        addOrderLineItems(orderLineItems.getOrderLineItems());
    }

    public Order(Long orderTableId, OrderStatus orderStatus,
        OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        checkStatusNotCompleted();
        this.orderStatus = orderStatus;
    }

    private void checkStatusNotCompleted() {
        if (orderStatus.isCompletion()) {
            throw new KitchenposException(KitchenposErrorCode.CANNOT_UPDATE_COMPLETED_ORDER);
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    public List<OrderLineItem> getOrderLineItemValues() {
        return orderLineItems.getOrderLineItems();
    }

    public String getOrderStatusName() {
        return orderStatus.name();
    }
}
