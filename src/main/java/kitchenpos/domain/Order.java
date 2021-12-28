package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

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

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        addOrderLineItems(orderLineItems.getOrderLineItems());
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
        OrderLineItems orderLineItems) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
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
        if (orderTable == null) {
            return null;
        }

        return orderTable.getId();
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.referenceOrder(this);
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
