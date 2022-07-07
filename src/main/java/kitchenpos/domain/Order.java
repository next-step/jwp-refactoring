package kitchenpos.domain;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.request.OrderLineItemRequest;
import kitchenpos.request.OrderRequest;
import org.springframework.util.CollectionUtils;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = orderedTime;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = orderedTime;
        this.orderStatus = orderStatus;
        this.orderLineItems = new ArrayList<>();
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(final Long id, final Long orderTableId) {
        this.id = id;
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderedTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.COOKING;
    }

    public static Order of(final OrderRequest orderRequest, final OrderTable orderTable) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
        return new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void checkOrderLineIsEmpty() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public void checkOrderLineItemsExists(final Long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException();
        }
    }

    public void checkOrderStatusComplete() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(final Order order) {
        this.orderStatus = order.getOrderStatus();
    }
}
