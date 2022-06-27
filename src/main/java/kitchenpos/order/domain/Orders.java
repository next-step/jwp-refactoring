package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Orders(long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;

        for (final OrderLineItemRequest orderLineItem : orderLineItemRequests) {
            this.orderLineItems.add(new OrderLineItem(this, orderLineItem.getMenuId(), orderLineItem.getQuantity()));
        }
    }

    protected Orders() {
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems.getAll();
    }

    public void updateStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("계산 완료 상태의 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }
}
