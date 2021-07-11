package kitchenpos.order;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.constant.OrderStatus;
import kitchenpos.exception.CompletedOrderException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    protected Order() {}

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;

    }

    public Order(Long id, OrderStatus orderStatus, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, orderStatus, null, orderLineItems);
    }

    public Order(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, null, null, orderLineItems);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime localDateTime,
            List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, localDateTime, orderLineItems);
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
        return Collections.unmodifiableList(orderLineItems);
    }

    public Boolean isImmutableOrder() {
        return OrderStatus.COOKING.equals(orderStatus) || OrderStatus.MEAL.equals(orderStatus);
    }

    public static Order create(Long orderTableId) {
        return new Order(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), Collections.emptyList());
    }

    public void updateStatus(OrderStatus orderStatus) {
        validationUpdate();
        this.orderStatus = orderStatus;
    }

    private void validationUpdate() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new CompletedOrderException();
        }
    }

    public void makeLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems != null) {
            orderLineItems.forEach(orderLineItem -> orderLineItem.assignOrder(this.id));
            this.orderLineItems = orderLineItems;
        }

    }
}
