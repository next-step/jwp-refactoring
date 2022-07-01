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
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Orders() {
    }

    Orders(Builder builder) {
        this.id = builder.id;
        this.orderTableId = builder.orderTableId;
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        this.orderLineItems = builder.orderLineItems;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new InvalidOrderStatusException();
        }
        this.orderStatus = orderStatus;
    }

    public void addOrderMenu(Long menuId, Quantity quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem.Builder(this)
                .setMenuId(menuId)
                .setQuantity(quantity)
                .builder();
        this.orderLineItems.add(orderLineItem);
    }

    public OrderResponse toOrderResponse() {
        final List<OrderLineItemResponse> orderLineItemResponses = this.orderLineItems.toOrderLineItemResponses();
        return new OrderResponse(this.id, this.orderTableId, this.orderStatus.name(), this.orderedTime,
                orderLineItemResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id) && Objects.equals(orderTableId, orders.orderTableId)
                && orderStatus == orders.orderStatus && Objects.equals(orderedTime, orders.orderedTime)
                && Objects.equals(orderLineItems, orders.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static class Builder {
        private Long id;
        private Long orderTableId;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private OrderLineItems orderLineItems = new OrderLineItems();

        public Builder(Long orderTableId) {
            this.orderTableId = orderTableId;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setOrderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public Builder setOrderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder setOrderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Builder setOrderLineItems(OrderLineItems orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Orders build() {
            return new Orders(this);
        }
    }
}
