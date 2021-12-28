package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputOrderDataErrorCode;
import kitchenpos.order.exceptions.InputOrderDataException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        validate(orderLineItems);
        this.orderTable = orderTable;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        this.orderStatus = OrderStatus.COOKING;
    }

    protected Order() {

    }

    public void startCooking() {
        checkEndOrder();
        this.orderStatus = OrderStatus.COOKING;
    }

    public void startMeal() {
        checkEndOrder();
        this.orderStatus = OrderStatus.MEAL;
    }

    public void endOrder() {
        checkEndOrder();
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

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
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

    private void checkEndOrder() {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_STATUS_DO_NOT_CHANGE_COMPLETION_TO_ANY_OTHER);
        }
    }

    private void validate(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getOrderLineItems())) {
            throw new InputOrderDataException(InputOrderDataErrorCode.THE_ORDER_LINE_IS_EMPTY);
        }
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
