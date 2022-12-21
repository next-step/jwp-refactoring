package kitchenpos.order.domain;

import kitchenpos.order.message.OrderMessage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Column(nullable = false)
    private Long orderTableId;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Order() {

    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);

        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.forEach(item -> item.changeOrder(this));
    }

    public static Order cooking(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order(orderTableId, orderLineItems);
        order.orderStatus = OrderStatus.COOKING;
        order.orderedTime = LocalDateTime.now();
        return order;
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if(orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(OrderMessage.CREATE_ERROR_ORDER_LINE_ITEMS_IS_EMPTY.message());
        }
    }

    public Long getId() {
        return this.id;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }

    public boolean isCookingOrMealState() {
        return !this.orderStatus.isCompletion();
    }

    public void changeState(OrderStatus orderStatus) {
        validateChangeState();
        this.orderStatus = orderStatus;
    }

    private void validateChangeState() {
        if(this.orderStatus.isCompletion()) {
            throw new IllegalArgumentException(OrderMessage.CHANGE_ERROR_STATE_IS_COMPLETION.message());
        }
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
        return id != null ? id.hashCode() : 0;
    }
}
