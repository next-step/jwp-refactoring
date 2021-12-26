package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.table.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {}

    private Order(Long id, OrderTable orderTable) {
        this.id = id;
        addOrderTable(orderTable);
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public static Order of(OrderTable orderTable) {
        return new Order(null, orderTable);
    }

    private void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.addOrder(this);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        checkOrderLineItemValidation(orderLineItems);

        for (OrderLineItem orderLineItem : orderLineItems) {
            addOrderLineItem(orderLineItem);
        }
    }

    private void addOrderTable(OrderTable orderTable) {
        if (!equalsOrderTable(orderTable)) {
            this.orderTable = orderTable;
            orderTable.addOrder(this);
        }
    }

    public boolean isComplete() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }

    public boolean equalsOrderTable(OrderTable orderTable) {
        if (Objects.isNull(this.orderTable)) {
            return false;
        }

        return this.orderTable.equals(orderTable);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        checkChangeOrderStatusValidation();
        this.orderStatus = orderStatus;
    }

    private void checkChangeOrderStatusValidation() {
        if (isComplete()) {
            throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
        }
    }

    private void checkOrderLineItemValidation(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 라인은 최소 1개 이상 필요합니다.");
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
        return orderLineItems.asList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
