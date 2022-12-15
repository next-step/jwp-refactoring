package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "orders")
@Entity
public class Order {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Enumerated
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Transient
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this.orderTable = new OrderTable(orderTableId);
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderedTime(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public boolean cannotChange() {
        return orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL;
    }

    public void updateOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
        if (Objects.nonNull(orderTable)) {
            this.orderTable.addOrder(this);
        }
    }
}
