package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderStatusCompleteException;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long id, OrderTable orderTable) {
        this.id = id;
        this.orderTable = orderTable;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private boolean isOrderStatusComplete() {
        return Objects.equals(OrderStatus.COMPLETION, this.getOrderStatus());
    }

    public void validateOrderStatusComplete() {
        if (isOrderStatusComplete()) {
            throw new OrderStatusCompleteException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return this.orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
