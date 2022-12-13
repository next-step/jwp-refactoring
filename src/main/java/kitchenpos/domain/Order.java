package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus;
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        orderTable.addOrder(this);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = new OrderLineItems();
    }

    public void addLineItems(List<OrderLineItem> orderLineItemsParam) {
        orderLineItemsParam.forEach(orderLineItem -> orderLineItem.changeOrder(this));
        orderLineItems.addOrderLineItems(orderLineItemsParam);
    }

    public List<Long> makeMenuIds() {
        return orderLineItems.makeMenuIds();
    }

    public void validateOrderLineItemsSizeAndMenuCount(long menuCount) {
        orderLineItems.validateOrderLineItemsSizeAndMenuCount(menuCount);
    }

    public Void changeStatus(String orderStatusParam) {
        validateOrderStatus();
        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusParam);
        this.orderStatus = orderStatus.name();
        return null;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}
