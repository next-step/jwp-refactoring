package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(orderTable, orderLineItems);
        this.id = id;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        orderTable.checkEmpty();
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        addOrderTable(orderTable);
    }

    public static Order empty() {
        return new Order();
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        checkOrderStatus(orderStatus);
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public void initialItems() {
        orderLineItems.forEach(orderLineItem -> orderLineItem.addOrder(this));
    }

    public boolean checkComplete() {
        return Objects.equals(OrderStatus.COMPLETION, this.orderStatus);
    }

    public void addOrderTable(OrderTable orderTable) {
        if(Objects.isNull(orderTable.getOrder())) {
            orderTable.addOrder(this);
        }
        this.orderTable = orderTable;
    }

    private void checkOrderStatus(OrderStatus orderStatus) {
        if(checkComplete()) {
            throw new IllegalArgumentException("주문이 완료된 상태입니다.");
        }
        if(Objects.isNull(orderStatus)) {
            throw new IllegalArgumentException("올바른 상태값을 입력해주시기 바랍니다.");
        }
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
}
