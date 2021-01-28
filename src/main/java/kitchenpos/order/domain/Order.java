package kitchenpos.order.domain;

import kitchenpos.order.exception.InvalidChangeOrderStatusException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public void addOrderIdToOrderLineItems() {
        orderLineItems.addOrderIdToOrderLineItems(id);
    }

    public void checkOrderStatusIsCompletion() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new InvalidChangeOrderStatusException("주문 상태가 계산 완료일 경우 변경할 수 없습니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

    public List<Long> getMenuIds() {
        return orderLineItems.getMenuIds();
    }
}
