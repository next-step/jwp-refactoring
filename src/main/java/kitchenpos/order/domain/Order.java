package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(orderStatus);
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }

        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(List<OrderLineItem> orderLineItems, OrderTable orderTable) {
        Order order = of(orderLineItems, orderTable, OrderStatus.COOKING);
        return order;
    }

    public static Order of(List<OrderLineItem> orderLineItems, OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
        orderTable.updateOrder(order);
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(order);
        }
        return order;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("이미 계산 완료된 주문입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isEqualToOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == orderStatus) {
            return true;
        }
        return false;
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
