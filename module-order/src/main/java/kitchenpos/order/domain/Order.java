package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems.addOrderLineItems(orderLineItems);

        this.orderLineItems.associateOrder(this);
    }

    public Order(Long orderTableId, OrderLineItems orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        isPossibleChangeOrderStatus();

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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void isPossibleChangeOrderStatus() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException("완료 상태의 주문의 상태를 변경할 수 없습니다.");
        }
    }
}
