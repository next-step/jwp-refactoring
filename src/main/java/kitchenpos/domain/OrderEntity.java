package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.Objects.requireNonNull;

@Entity(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public OrderEntity(Long orderTableId) {
        this.orderTableId = requireNonNull(orderTableId, "orderTableId");
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    protected OrderEntity() {
    }

    public void addOrderLineItems(List<OrderLineItemEntity> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems.addAll(this, orderLineItems);
    }

    private void validateOrderLineItems(List<OrderLineItemEntity> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new EmptyOrderLineItemsException();
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

    public List<OrderLineItemEntity> getOrderLineItems() {
        return orderLineItems.get();
    }
}
