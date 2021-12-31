package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyOrderStatusException;
import kitchenpos.common.exceptions.EmptyOrderTableException;
import org.springframework.data.annotation.CreatedDate;

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

    private Long orderTableId;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = OrderLineItems.empty();

    protected Order() {
    }

    private Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        validate(orderTableId, orderStatus);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final Long orderTableId, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        return new Order(null, orderTableId, orderStatus, orderLineItems);
    }

    public static Order of(final Long id, final Long orderTableId, final OrderStatus orderStatus) {
        return new Order(id, orderTableId, orderStatus, OrderLineItems.empty());
    }

    public static Order of(final Long id, final Long orderTableId, final OrderStatus orderStatus, final OrderLineItems orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public static Order from(final Long orderTableId) {
        return new Order(null, orderTableId, OrderStatus.COOKING, OrderLineItems.empty());
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItemList) {
        orderLineItemList.forEach(this::addOrderLineItem);
    }

    private void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItem.decideOrder(this);
        this.orderLineItems.add(orderLineItem);
    }

    private void validate(final Long orderTableId, OrderStatus orderStatus) {
        if (Objects.isNull(orderTableId)) {
            throw new EmptyOrderTableException();
        }
        if (Objects.isNull(orderStatus)) {
            throw new EmptyOrderStatusException();
        }
    }

    public void updateStatus(final OrderStatus updateStatus) {
        this.orderStatus.validateStatus(updateStatus);
        this.orderStatus = updateStatus;
    }

    public boolean existsCookingOrMeal() {
        return this.orderStatus.existsOrderStatus();
    }

    public Long getId() {
        return this.id;
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public OrderLineItems getOrderLineItems() {
        return this.orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
