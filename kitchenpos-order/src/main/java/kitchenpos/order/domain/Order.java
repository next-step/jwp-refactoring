package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    private static final String ERROR_MESSAGE_COMPLETED = "이미 완료된 주문은 상태를 바꿀 수 없습니다.";
    private static final String ERROR_MESSAGE_TABLE_EMPTY = "테이블 정보가 올바르지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTableId);

        this.orderTableId = orderTableId;
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;

        addOrderLineItems(orderLineItems);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTableId);

        this.orderTableId = orderTableId;
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = orderStatus;

        addOrderLineItems(orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_COMPLETED);
        }

        this.orderStatus = orderStatus;
    }

    private void validateOrder(Long orderTableId) {
        if (orderTableId == null || orderTableId < 1) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TABLE_EMPTY);
        }
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addOrderLineItems(orderLineItems);
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
