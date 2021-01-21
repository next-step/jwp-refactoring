package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderLineItem> orderLineItems = new LinkedList<>();

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
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

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
        orderLineItem.setOrder(this);
    }

    protected Order() {
    }

    public Order(OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public boolean isRestrictedChangeEmpty() {
        return orderStatus.isRestrictedChangeEmpty();
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if(this.orderStatus.isRestrictedChangeOrderStatus()) {
            throw new IllegalArgumentException("이미 왼료상태인 주문은 상태 변경이 불가합니다.");
        }

        this.orderStatus = orderStatus;
    }

    public static Order createOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 주문 테이블입니다.");
        }

        Order order = new Order(OrderStatus.COOKING, LocalDateTime.now());
        order.setOrderTable(orderTable);
        orderLineItems.forEach(order::addOrderLineItem);
        return order;
    }
}
