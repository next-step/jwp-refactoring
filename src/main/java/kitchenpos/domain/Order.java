package kitchenpos.domain;

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
import javax.persistence.Table;
import kitchenpos.dto.OrderLineItemResponse;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(final Long id,
                 final Long orderTableId) {
        this.id = id;
        this.orderTableId = orderTableId;
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.makeRelations(this, orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus targetStatus) {
        validateStatusToChange(targetStatus);
        this.orderStatus = targetStatus;
    }

    private void validateStatusToChange(final OrderStatus targetStatus) {
        if (null == targetStatus) {
            throw new IllegalArgumentException("변경하려는 상태값이 입력되지 않았습니다.");
        }
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalStateException("완료된 주문의 상태는 변경할 수 없습니다.");
        }
        if (orderStatus.equals(targetStatus)) {
            throw new IllegalStateException("변경하려는 주문의 상태가 현재 상태와 같습니다.");
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems.getResponses();
    }
}
