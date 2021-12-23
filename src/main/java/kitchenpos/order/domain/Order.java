package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kitchenpos.common.exception.Message;
import kitchenpos.table.domain.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    public static Order createCook(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderLineItems);
    }

    public static Order createCook(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }

    private Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {

        validRequireOrderTable(orderTable);

        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        association(orderLineItems);
        this.orderLineItems = OrderLineItems.of(orderLineItems);
    }

    protected Order() {
    }

    private void validRequireOrderTable(OrderTable orderTable) {
        if (Objects.isNull(orderTable)) {
            throw new IllegalArgumentException(Message.ORDER_TABLE_IS_NOT_NULL.getMessage());
        }
    }

    public void changeOrderStatus(final String changeOrderStatus) {
        if (OrderStatus.isEqualsCompletion(getOrderStatus())) {
            throw new IllegalArgumentException(Message.ORDER_STATUS_IS_NOT_COMPLETION.getMessage());
        }
        this.orderStatus = OrderStatus.valueOf(changeOrderStatus);
    }

    private void association(List<OrderLineItem> orderLineItems) {
        orderLineItems.stream()
            .forEach(s -> s.setOrder(this));
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
        return orderLineItems.getList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
