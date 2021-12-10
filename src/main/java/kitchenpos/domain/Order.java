package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "seq")
    private List<OrderLineItem> orderLineItems;

    protected Order() {

    }

    private Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Long getId() {
        return this.id;
    }

    public OrderTable getOrderTable() {
        return this.orderTable;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return this.orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }
}
