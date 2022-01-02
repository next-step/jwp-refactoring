package kitchenpos.order.domain;

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

    @Column(name = "order_table_id", length = 20, nullable = false)
    private Long orderTable;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    protected Order() {
    }

    public Order(final Long orderTable, final List<OrderLineItem> orderLineItems, final OrderStatus orderStatus) {
        this.orderTable = orderTable;
        this.orderLineItems = orderLineItems;
        this.orderStatus = orderStatus;
    }

    public Order(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrder() {
        orderLineItems.stream()
                .forEach(orderLineItem -> orderLineItem.addOrder(this));
    }

    public void changeOrderStatus(OrderStatus changeOrderStatus, OrderValidator orderValidator) {
        orderValidator.changeOrderStatusValidator(this);
        this.orderStatus = changeOrderStatus;
    }

    public static Order toOrderCooking(final Long orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems, OrderStatus.COOKING);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

}
