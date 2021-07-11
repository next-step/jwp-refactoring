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
    private String orderStatus;
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long id, OrderTable orderTable) {
        this.id = id;
        this.orderTable = orderTable;
    }

    public Order(Long id, OrderTable orderTable, String orderStatus) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private boolean isOrderStatusComplete() {
        return Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus());
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void changeOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
