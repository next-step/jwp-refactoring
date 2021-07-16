package kitchenpos.order.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.order.domain.value.OrderLineItems;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.exception.OrderStatusCompletionException;
import kitchenpos.table.domain.entity.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus = OrderStatus.COOKING.name();

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, String orderStatus,
        OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        orderLineItems.toOrder(this);
    }

    public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
        orderLineItems.toOrder(this);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void changeOrderStatus(String orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (this.orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new OrderStatusCompletionException();
        }
    }

    public List<OrderLineItem> getOrderLineItemList() {
        return orderLineItems.getValue();
    }
}
