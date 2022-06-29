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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.InvalidOrderStatusException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

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
        validateOrderTable(builder.orderTable);
        this.id = builder.id;
        this.orderTable = builder.orderTable;
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        this.orderLineItems = builder.orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new InvalidOrderStatusException();
        }
        this.orderStatus = orderStatus;
    }

    public void addOrderMenu(Menu menu, Quantity quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem.Builder(this)
                .setMenu(menu)
                .setQuantity(quantity)
                .builder();
        this.orderLineItems.add(orderLineItem);
    }

    public OrderResponse toOrderResponse() {
        final OrderTableResponse orderTableResponse = this.orderTable.toOrderTableResponse();
        final List<OrderLineItemResponse> orderLineItemResponses = this.orderLineItems.toOrderLineItemResponses();
        return new OrderResponse(this.id, orderTableResponse, this.orderStatus.name(), this.orderedTime, orderLineItemResponses);
    }

    private void validateOrderTable(OrderTable orderTable) {
        orderTable.validateEmpty();
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
        return Objects.equals(id, orders.id) && Objects.equals(orderTable.getId(), orders.orderTable.getId())
                && orderStatus == orders.orderStatus && Objects.equals(orderedTime, orders.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime);
    }

    public static class Builder {
        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private OrderLineItems orderLineItems = new OrderLineItems();

        public Builder(OrderTable orderTable) {
            this.orderTable = orderTable;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setOrderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
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
