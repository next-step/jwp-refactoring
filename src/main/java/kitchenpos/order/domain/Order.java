package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreationTimestamp
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        validate(orderTable);
        orderTable.addOrder(this);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(orderLineItems.getOrderLineItems(), this);
    }

    public static Order of(OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(orderTable, OrderStatus.COOKING, orderLineItems);
    }

    public boolean isNotCompletion() {
        return !OrderStatus.COMPLETION.equals(orderStatus);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        changeStatusValidate(this.orderStatus);
        this.orderStatus = orderStatus;
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }
    }

    private void changeStatusValidate(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION.equals(orderStatus)) {
            throw new IllegalArgumentException("계산 완료 주문의 경우 상태를 변경할 수 없습니다.");
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
