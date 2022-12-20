package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.order.exception.OrderException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.order.exception.OrderExceptionType.BEFORE_COMPLETE_ORDER_STATUS;
import static kitchenpos.order.exception.OrderExceptionType.COMPLETE_ORDER_STATUS;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(OrderBuilder builder) {
        this.id = builder.id;
        this.orderTableId = builder.orderTableId;
        this.orderStatus = builder.orderStatus;
        this.orderLineItems = builder.orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateCompleteStatus();
        this.orderStatus = orderStatus;
    }

    private void validateCompleteStatus() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new OrderException(COMPLETE_ORDER_STATUS);
        }
    }

    public void validateBeforeCompleteStatus() {
        if (orderStatus.equals(OrderStatus.MEAL) || orderStatus.equals(OrderStatus.COOKING)) {
            throw new OrderException(BEFORE_COMPLETE_ORDER_STATUS);
        }
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Long id;
        private Long orderTableId;
        private OrderStatus orderStatus;
        private final List<OrderLineItem> orderLineItems = new ArrayList<>();

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems.addAll(orderLineItems);
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}

