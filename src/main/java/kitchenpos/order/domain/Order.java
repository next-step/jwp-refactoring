package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Order {

    private static final String ORDER_ALREADY_COMPLETED_EXCEPTION = "해당 주문은 이미 완료되었습니다.";
    private static final String TABLE_IS_NOT_EMPTY_EXCEPTION = "해당 테이블은 비어 있습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderStatus;
    private LocalDateTime orderedTime;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {

    }

    public Order(String orderStatus, LocalDateTime orderedTime, OrderTable orderTable) {
        validateOrderTableStatus(orderTable);
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTable = orderTable;
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(TABLE_IS_NOT_EMPTY_EXCEPTION);
        }
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItemList) {
        orderLineItems.addList(orderLineItemList);
    }

    public void updateOrderStatus(String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException(ORDER_ALREADY_COMPLETED_EXCEPTION);
        }
        this.orderStatus = orderStatus;
    }

    public boolean isOrderComplete() {
        if (OrderStatus.COMPLETION.name().equals(orderStatus)) {
            return true;
        }

        return false;
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
