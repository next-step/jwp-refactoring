package kitchenpos.domain.order;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = Lists.newArrayList();

    protected Order() {}

    private Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;

        this.orderLineItems.forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }

    private Order(Long orderTableId, OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;

        this.orderLineItems.forEach(orderLineItem -> orderLineItem.mappedByOrder(this));
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static Order of(Long orderTableId, OrderStatus orderStatus) {
        return new Order(orderTableId, orderStatus);
    }

    public static Order of(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                           List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
