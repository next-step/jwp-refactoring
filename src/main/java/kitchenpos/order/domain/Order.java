package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"), nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    @Column(nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    private Order(Long id, Long orderTableId, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, OrderLineItems.of(orderLineItems));
    }

    public void occurred(OrderValidator orderValidator) {
        orderValidator.validate(this);
    }

    public void changeOrderStatus(OrderValidator orderValidator, OrderStatus orderStatus) {
        orderValidator.validateChangeable(this);
        this.orderStatus = orderStatus;
    }

    public boolean isComplete() {
        return orderStatus.isComplete();
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
        return orderLineItems.asList();
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
