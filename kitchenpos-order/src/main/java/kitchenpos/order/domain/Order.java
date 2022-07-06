package kitchenpos.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    private static final String ORDER_ITEM_IS_ESSENTIAL = "주문 항목은 필수입니다";
    private static final String ORDER_STATUS_IS_COMPLETION = "계산 완료된 주문은 변경할 수 없습니다";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = OrderStatus.COOKING;
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(ORDER_ITEM_IS_ESSENTIAL);
        }
        orderLineItems.forEach(this::addOrderLineItem);
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

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.setOrder(this);
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException(ORDER_STATUS_IS_COMPLETION);
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
    }
}
