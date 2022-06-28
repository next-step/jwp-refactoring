package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.table.domain.OrderTable;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    protected Order() {}

    private Order(OrderTable orderTable) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order from(OrderTable orderTable) {
        return new Order(orderTable);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(this, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getValue();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public boolean isComplete() {
        return this.orderStatus.isComplete();
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
