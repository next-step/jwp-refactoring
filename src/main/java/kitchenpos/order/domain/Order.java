package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
        validateOrderLineItems();
    }

    public Order(Long id, OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        addOrderLineItems(orderLineItems);
        validateOrderLineItems();
    }

    private void validateOrder(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> {
            this.orderLineItems.add(orderLineItem);
            orderLineItem.setOrder(this);
        });
    }

    private void validateOrderLineItems() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.isCompleted(orderStatus)) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }

    public void validateCompleted() {
        if (!OrderStatus.isCompleted(orderStatus)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateNotProcessing() {
        if (!OrderStatus.isMeal(orderStatus) && !OrderStatus.isCooking(orderStatus)) {
            return;
        }

        throw new IllegalArgumentException();
    }
}
