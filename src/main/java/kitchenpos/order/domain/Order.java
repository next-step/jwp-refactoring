package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(){}

    private Order(OrderBuilder builder){
        validateOrderTable(builder.orderTable);
        this.id = builder.id;
        this.orderTable = builder.orderTable;
        this.orderStatus = builder.orderStatus;
        this.orderedTime = builder.orderedTime;
        this.orderLineItems = builder.orderLineItems;
    }

    private void validateOrderTable(OrderTable orderTable){
        if(Objects.isNull(orderTable) || orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> this.orderLineItems.add(orderLineItem));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable(){
        return orderTable;
    }

    public Long getOrderTableId() {
        if(Objects.isNull(orderTable)){
            return null;
        }
        return orderTable.getId();
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderBuilder builder(){
        return new OrderBuilder();
    }

    public void changeOrderStatus(String orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public static class OrderBuilder {
        private Long id;
        private OrderTable orderTable;
        private String orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems = new ArrayList<>();

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public OrderBuilder orderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderBuilder orderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems.addAll(orderLineItems);
            return this;
        }

        public Order build(){
            return new Order(this);
        }
    }
}

