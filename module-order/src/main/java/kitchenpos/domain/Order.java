package kitchenpos.domain;

import kitchenpos.guestordertable.GuestOrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Long orderTableId;
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    Order() {
    }

    public Order(GuestOrderTable orderTable) {
        this.orderTableId = orderTable.getId();
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
        orderLineItems.forEach(it -> {
            it.setOrder(this);
        });
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }
}
