package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.findAll();
    }

    public void updateOrderStatus(String orderStatus) {
        checkStatusCompletion();
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public void updateOrderLineItems(OrderLineItems orderLineItems) {
        orderLineItems.updateOrder(this);
        this.orderLineItems = orderLineItems;
    }

    private void checkStatusCompletion() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("계산완료된 주문의 상태는 변경할 수 없습니다.");
        }
    }

    public static class Builder {
        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private OrderLineItems orderLineItems;

        public Builder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
            this.orderTable = checkValidTable(orderTable);
            this.orderLineItems = checkValidOrderLineItems(orderLineItems);
            this.orderStatus = OrderStatus.COOKING;
        }

        public Order.Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Order.Builder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public Order.Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        private OrderTable checkValidTable(OrderTable orderTable) {
            if (orderTable.isEmpty()) {
                throw new IllegalArgumentException("빈 테이블은 주문할 수 없습니다.");
            }
            return orderTable;
        }

        private OrderLineItems checkValidOrderLineItems(List<OrderLineItem> orderLineItems) {
            if (CollectionUtils.isEmpty(orderLineItems)) {
                throw new IllegalArgumentException("주문에는 1개 이상의 메뉴가 포함되어야합니다.");
            }
            return new OrderLineItems(orderLineItems);
        }

        public Order build() {
            return new Order(this);
        }
    }

    private Order (Order.Builder builder) {
        this.id = builder.id;
        this.orderTable = builder.orderTable;
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        updateOrderLineItems(builder.orderLineItems);
    }
}
