package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        Assert.notNull(orderTableId, "테이블 ID는 비어있을수 없습니다.");
        Assert.notNull(orderStatus, "주문 상태는 비어있을수 없습니다.");
        Assert.notEmpty(orderLineItems, "주문 항목는 비어있을수 없습니다.");

        orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(this));

        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = OrderLineItems.of(orderLineItems);
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public static Order of(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if (isCompletionStatus()) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }

    private boolean isCompletionStatus() {
        return this.orderStatus.equals(OrderStatus.COMPLETION);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
