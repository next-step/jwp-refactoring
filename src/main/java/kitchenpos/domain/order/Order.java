package kitchenpos.domain.order;

import kitchenpos.domain.order.exceptions.InvalidTryChangeOrderStatusException;
import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
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

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (!this.orderStatus.canChange()) {
            throw new InvalidTryChangeOrderStatusException("계산 완료된 주문의 상태를 바꿀 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getList();
    }
}
