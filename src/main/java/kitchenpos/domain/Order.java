package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public static Order of(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return null == orderTable ? null : orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus.toString();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void setOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
