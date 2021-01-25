package kitchenpos.orders.domain;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;

import kitchenpos.table.domain.OrderTable;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();;

    public Orders() {
    }

    public Orders(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime,
        OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Orders(OrderTable orderTable, String orderStatus) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public Orders(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Orders(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this(orderTable, orderStatus, orderedTime, null);
    }

    public Orders(OrderTable orderTable, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public boolean isStatusCompletion(){
        return orderStatus.equals(OrderStatus.COMPLETION.name());
    }

    public void add(OrderLineItems orderLineItems) {
        this.orderLineItems.add(this, orderLineItems);
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if(this.orderStatus == OrderStatus.COMPLETION.name()){
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus.name();
    }
}
