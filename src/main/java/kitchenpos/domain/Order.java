package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kitchenpos.dto.OrderRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = {"order"} , allowSetters = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(OrderTable orderTable) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        orderTable.checkIsEmpty();
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        orderTable.checkIsEmpty();
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void addOrderLineItems(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public void changeStatus(OrderRequest orderRequest) {

        if (Objects.equals(OrderStatus.COMPLETION, orderRequest.getOrderStatus())) {
            throw new IllegalArgumentException("주문이 완료 상태입니다.");
        }
        this.orderStatus = orderRequest.getOrderStatus();
    }

    public void changeOrderStatusMeal() {
        this.orderStatus = OrderStatus.MEAL;
    }

    public void checkOrderStatusCookingOrMeal() {
        if(orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL){
            throw new IllegalArgumentException("주문이 조리중이거나 식사중입니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTable, order.orderTable) && orderStatus == order.orderStatus && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderTable=" + orderTable +
                ", orderStatus=" + orderStatus +
                ", orderedTime=" + orderedTime +
                ", orderLineItems=" + orderLineItems +
                '}';
    }
}
