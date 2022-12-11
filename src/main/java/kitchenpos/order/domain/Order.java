package kitchenpos.order.domain;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {

    }

    public Order(OrderTable orderTable, OrderStatus orderStatus) {
        validate(orderTable);

        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    private void validate(OrderTable orderTable) {
        if(Objects.isNull(orderTable)) {
            throw new IllegalArgumentException();
        }

        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void order(List<OrderLineItem> orderLineItems) {
        if(CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        orderLineItems.forEach(orderLineItem -> addOrderLineItem(orderLineItem));
    }

    void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.addOrderLineItem(this, orderLineItem);
    }

    public void checkOngoingOrderTable() {
        if(orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException();
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

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
