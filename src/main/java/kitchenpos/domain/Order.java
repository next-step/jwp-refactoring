package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItemRequests) {
        this(orderTable, orderLineItemRequests);
        this.id = id;
        this.orderStatus = orderStatus;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.orderStatus = OrderStatus.COOKING;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeStatus(OrderStatus status) {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = status;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
    }
}
