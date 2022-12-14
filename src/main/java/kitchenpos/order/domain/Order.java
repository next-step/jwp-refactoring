package kitchenpos.order.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(){}

    private Order(OrderBuilder builder){
        validateOrderTable(builder.orderTable);
        this.id = builder.id;
        this.orderTable = builder.orderTable;
        this.orderStatus = builder.orderStatus;
        this.orderLineItems = builder.orderLineItems;
    }

    private void validateOrderTable(OrderTable orderTable){
        if(Objects.isNull(orderTable) || orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable(){
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public void validateComplete() {
        if(orderStatus.equals(OrderStatus.MEAL) || orderStatus.equals(OrderStatus.COOKING)){
            throw new IllegalArgumentException();
        }
    }

    public static OrderBuilder builder(){
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private final List<OrderLineItem> orderLineItems = new ArrayList<>();

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
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

