package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        changeOrderLineItem(orderLineItems);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    private Order(Long id, OrderTable orderTable) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(Long id, OrderTable orderTable) {
        orderTable.validIsEmpty();
        return new Order(id, orderTable);
    }

    public static Order of(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        orderTable.validIsEmpty();
        return new Order(orderTable, orderLineItems);
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validIsNotCompletion(orderStatus);
        this.orderStatus = orderStatus;
    }

    public void validIsNotCompletion(OrderStatus orderStatus) {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("완료 상태인 경우 주문 상태를 변경할 수 없습니다.");
        }
    }

    public void changeOrderLineItem(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.changeOrderLineItem(this));
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
