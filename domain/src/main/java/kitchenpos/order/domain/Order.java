package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
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

    public static Order createOrder(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 주문 테이블입니다.");
        }

        return new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
    }
}
