package kitchenpos.order.domain;

import kitchenpos.order.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@AttributeOverride(name = "createdDate", column = @Column(name = "ordered_time"))
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {

    }

    public Order(Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, orderStatus, new OrderLineItems(orderLineItems));
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("주문 상태가 이미 '계산 완료' 상태입니다.");
        }

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
        return super.getCreatedDate();
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
