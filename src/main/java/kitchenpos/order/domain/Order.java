package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(OrderTable orderTable) {
        this.orderTable = orderTable;
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

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void addOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems.addAll(orderLineItems.orderLineItems());
    }

    private boolean isOrderStatusComplete() {
        return Objects.equals(OrderStatus.COMPLETION.name(), this.getOrderStatus());
    }

    public void validateOrderStatusComplete() {
        if (isOrderStatusComplete()) {
            throw new IllegalArgumentException();
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.orderLineItems();
    }
}
