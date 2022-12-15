package kitchenpos.order.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {}

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus,
                 LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.EMPTY_TABLE.getMessage());
        }
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public void checkCookingOrMeal() {
        if(orderStatus.equals(OrderStatus.COOKING) ||
                orderStatus.equals(OrderStatus.MEAL)) {
            throw new IllegalArgumentException(ExceptionMessage.COOKING_OR_MEAL.getMessage());
        }
    }

    public void updateOrder() {
        orderLineItems.updateOrder(this);
    }

    public void change(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_COMPLETE.getMessage());
        }
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

}
