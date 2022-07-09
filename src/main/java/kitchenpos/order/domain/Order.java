package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
import kitchenpos.common.exception.InvalidValueException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        this(id, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        validateOrderLineItemRequestEmpty(orderLineItems);

        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItemList) {
        OrderLineItems orderLineItems = convertToOrderLineItems(orderLineItemList);

        validateOrderLineItemRequestEmpty(orderLineItems);

        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public void updateOrderStatus(String status) {
        if(Objects.equals(OrderStatus.COMPLETION.name(), orderStatus.name())) {
            throw new IllegalArgumentException();
        }

        orderStatus = OrderStatus.valueOf(status);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    private void validateOrderLineItemRequestEmpty(OrderLineItems orderLineItems) {
        if(orderLineItems.isEmpty()) {
            throw new InvalidValueException();
        }
    }

    private OrderLineItems convertToOrderLineItems(List<OrderLineItem> orderLineItems) {
        for(OrderLineItem orderLineitem: orderLineItems) {
            orderLineitem.updateOrder(this);
        }

        return new OrderLineItems(orderLineItems);
    }
}
