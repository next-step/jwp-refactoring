package kitchenpos.order.domain;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.table.domain.OrderTable;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    private Order(Builder builder) {
        this(builder.id, builder.orderTable, builder.orderStatus, builder.orderedTime, builder.orderLineItems);
    }

    protected Order() {
    }

    private Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        this(null, orderTable, orderLineItems);
    }

    private Order(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        this(id, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        validate();
    }

    public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    private void validate() {
        validateNonNullFields();
        validateOrderLineItems();
        validateOrderTable();
    }

    private void validateNonNullFields() {
        if (orderTable == null || orderStatus == null || orderedTime == null || orderLineItems == null) {
            throw new IllegalArgumentException("주문 테이블, 주문 상태, 주문 시간, 주문 항목은 주문의 필수 사항입니다.");
        }
    }

    private void validateOrderLineItems() {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 없는 주문을 생성할 수 없습니다.");
        }
    }

    private void validateOrderTable() {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에서 주문을 받을 수 없습니다.");
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
        return orderTable.getId();
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
        private final OrderTable orderTable;
        private final OrderStatus orderStatus;
        private final LocalDateTime orderedTime;
        private final OrderLineItems orderLineItems;

        public Builder() {
            this(null, null, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.create());
        }

        public Builder(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                       OrderLineItems orderLineItems) {
            this.id = id;
            this.orderTable = orderTable;
            this.orderStatus = orderStatus;
            this.orderedTime = orderedTime;
            this.orderLineItems = orderLineItems;
        }

        public Builder id(Long id) {
            return new Builder(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderTable(OrderTable orderTable) {
            return new Builder(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            return new Builder(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            return new Builder(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }

        public Builder orderLineItems(OrderLineItems orderLineItems) {
            return new Builder(id, orderTable, orderStatus, orderedTime, orderLineItems);
        }

        public Order build() {
            return new Order(this);
        }
    }
}
