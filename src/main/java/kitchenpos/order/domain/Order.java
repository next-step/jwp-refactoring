package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {
    private static final String ALREADY_COMPLETION_ORDER = "이미 계산 완료 된 주문입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(orderTable, orderStatus, orderedTime, new OrderLineItems(orderLineItems));
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }


    public Long id() {
        return id;
    }

    public OrderTable orderTable() {
        return orderTable;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.orderLineItems();
    }

    public void mappingOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        validateCompletion();
        changeTableStatusIfCompletion(orderStatus);
        this.orderStatus = orderStatus;
    }

    private void validateCompletion() {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException(ALREADY_COMPLETION_ORDER);
        }
    }

    private void changeTableStatusIfCompletion(OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.COMPLETION) {
            orderTable.changeTableStatus(TableStatus.COMPLETION);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTable, order.orderTable) && Objects.equals(orderStatus, order.orderStatus) && Objects.equals(orderedTime, order.orderedTime) && Objects.equals(orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }
}