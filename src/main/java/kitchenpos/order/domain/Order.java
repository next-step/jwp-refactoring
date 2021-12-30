package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.order.domain.OrderStatus.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ORDER_TABLE_ID", nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order(Long orderTableId, OrderLineItems orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
    }

    protected Order() {

    }

    public void startCooking() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public void startMeal() {
        this.orderStatus = OrderStatus.MEAL;
    }

    public void endOrder() {
        this.orderStatus = OrderStatus.COMPLETION;
    }

    public boolean isCooking() {
        return this.orderStatus == OrderStatus.COOKING;
    }

    public boolean isEating() {
        return this.orderStatus == OrderStatus.MEAL;
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
        return orderLineItems.getOrderLineItems();
    }

    public Long getOrderTableId() {
        return this.orderTableId;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        if(this.orderStatus == COMPLETION){
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER);
        }

        if (orderStatus == COOKING) {
            this.startCooking();
            return;
        }

        if (orderStatus == MEAL) {
            this.startMeal();
            return;
        }

        this.endOrder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
