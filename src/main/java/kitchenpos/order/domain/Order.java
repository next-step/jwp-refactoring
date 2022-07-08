package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    private Order(Builder builder) {
        this(builder.id, builder.orderTableId, builder.orderStatus, builder.orderedTime, builder.orderLineItems);
    }

    protected Order() {
    }

    private Order(Long orderTableId, OrderLineItems orderLineItems) {
        this(null, orderTableId, orderLineItems);
    }

    private Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        this(id, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        validate();
    }

    public static Order of(Long orderTableId, OrderLineItems orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    private void validate() {
        validateNonNullFields();
    }

    private void validateNonNullFields() {
        if (orderTableId == null || orderStatus == null || orderedTime == null || orderLineItems == null) {
            throw new IllegalArgumentException("주문 테이블, 주문 상태, 주문 시간, 주문 항목은 주문의 필수 사항입니다.");
        }
    }

    public void updateOrderStatus(String orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException("계산 완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    private boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
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
        return orderLineItems.value();
    }

    public static class Builder {
        private final Long id;
        private final Long orderTableId;
        private final OrderStatus orderStatus;
        private final LocalDateTime orderedTime;
        private final OrderLineItems orderLineItems;

        public Builder() {
            this(null, null, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.create());
        }

        public Builder(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                       OrderLineItems orderLineItems) {
            this.id = id;
            this.orderTableId = orderTableId;
            this.orderStatus = orderStatus;
            this.orderedTime = orderedTime;
            this.orderLineItems = orderLineItems;
        }

        public Builder id(Long id) {
            return new Builder(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderTableId(Long orderTableId) {
            return new Builder(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            return new Builder(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            return new Builder(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderLineItems(OrderLineItems orderLineItems) {
            return new Builder(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }

        public Order build() {
            return new Order(this);
        }
    }
}
