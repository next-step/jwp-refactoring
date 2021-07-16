package kitchenpos.order.domain;

import kitchenpos.table.application.OrderTableNotFoundException;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    private static final String CHANGE_NOT_ALLOWED = "주문완료에서는 변경을 할 수 없습니다.";
    private static final String NOT_EXIST_ORDER_LINE_TABLES = "존재하지 않는 주문 테이블";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.registerOrder(id);
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);

        Order order = Order.builder()
                .orderTableId(orderTableId)
                .orderStatus(OrderStatus.COOKING)
                .orderLineItems(new OrderLineItems())
                .orderedTime(LocalDateTime.now())
                .build();

        for (OrderLineItem orderLineItem : orderLineItems) {
            order.addOrderLineItem(orderLineItem);
        }
        return order;
    }

    private static void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderTableNotFoundException(NOT_EXIST_ORDER_LINE_TABLES);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderState();
        this.orderStatus = orderStatus;
    }

    private void validateOrderState() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION)) {
            throw new IllegalStateException(CHANGE_NOT_ALLOWED);
        }
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
        return orderLineItems.getOrderLineItems();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Order order = new Order();

        public Builder id(Long id) {
            order.id = id;
            return this;
        }

        public Builder orderTableId(Long orderTableId) {
            order.orderTableId = orderTableId;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            order.orderStatus = orderStatus;
            return this;
        }

        public Builder orderLineItems(OrderLineItems orderLineItems) {
            order.orderLineItems = orderLineItems;
            return this;
        }

        public Builder orderedTime(LocalDateTime orderedTime) {
            order.orderedTime = orderedTime;
            return this;
        }

        public Order build() {
            return order;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTableId=" + orderTableId +
                ", orderStatus=" + orderStatus +
                ", orderLineItems=" + orderLineItems +
                ", orderedTime=" + orderedTime +
                '}';
    }
}
