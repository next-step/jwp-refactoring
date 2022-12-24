package kitchenpos.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.common.ErrorMessage.COMPLETED_ORDER;
import static kitchenpos.common.ErrorMessage.TABLE_HAVE_ONGOING_ORDER;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
    }

    public static Order of(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateCompletionOrderStatus();
        this.orderStatus = orderStatus;
    }

    public void validateCompletionOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException(COMPLETED_ORDER.getMessage());
        }
    }

    public void validateOnGoingOrderStatus() {
        if (!Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException(TABLE_HAVE_ONGOING_ORDER.getMessage());
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
