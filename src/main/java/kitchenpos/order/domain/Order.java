package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        checkValidation(orderTable, orderLineItems);

        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems.addOrderLineItems(orderLineItems);

        this.orderLineItems.associateOrder(this);
    }

    public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        isPossibleChangeOrderStatus();

        this.orderStatus = orderStatus;
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    private void checkValidation(OrderTable orderTable, OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문에 주문 목록이 존재하지 않습니다.");
        }
        if (orderTable == null || orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 자리의 테이블에 주문을 할 수 없습니다.");
        }
    }

    private void isPossibleChangeOrderStatus() {
        if (OrderStatus.COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException("완료 상태의 주문의 상태를 변경할 수 없습니다.");
        }
    }
}
