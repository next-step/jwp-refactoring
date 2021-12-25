package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Entity
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
    private OrderLineItems orderLineItems = OrderLineItems.empty();

    protected Order() {
    }

    private Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        addOrderLineItems(orderLineItems.getOrderLineItems());
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this(orderTableId, orderStatus, orderedTime, orderLineItems);
        this.id = id;
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException("주문 상태가 completion인 경우 주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem ->
        {
            orderLineItem.setOrder(this);
            this.orderLineItems.add(orderLineItem);
        });
    }
}
