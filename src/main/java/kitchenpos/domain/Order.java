package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @CreatedDate
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus) {
        this(null, orderTable, orderStatus);
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void appendOrderLineItems(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void chaangeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
