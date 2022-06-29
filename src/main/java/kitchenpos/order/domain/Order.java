package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
