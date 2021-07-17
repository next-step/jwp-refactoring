package domain.order;

import domain.order.exception.CannotChangeOrderStatusException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id")
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateOrderable();
        this.orderStatus = orderStatus;
    }

    private void validateOrderable() {
        if (Objects.equals(orderStatus, OrderStatus.COMPLETION)) {
            throw new CannotChangeOrderStatusException();
        }
    }

    public boolean isCookingOrMeal() {
        return Objects.equals(orderStatus, OrderStatus.COOKING) || Objects.equals(orderStatus, OrderStatus.MEAL);
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
        return orderLineItems.getUnmodifiableList();
    }
}
