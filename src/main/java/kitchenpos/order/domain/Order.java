package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.global.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    protected Order() {
    }

    public Order(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void saveOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public void changeOrderStatus(OrderStatus changeOrderStatus) {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new CannotChangeOrderStatusException(String.format("order status is %s", orderStatus.name()));
        }
        this.orderStatus = changeOrderStatus;
    }

    public void saveOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
        orderTable.addOrder(this);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

}
