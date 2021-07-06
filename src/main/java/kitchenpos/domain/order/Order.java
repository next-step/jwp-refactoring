package kitchenpos.domain.order;

import kitchenpos.exception.InvalidOrderLineItemsException;
import kitchenpos.exception.InvalidOrderTableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    // for JPA
    public Order() {
    }

    private Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        if (orderTable.isEmpty()) {
            throw new InvalidOrderTableException("Should have not orderTable empty");
        }
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderLineItemsException("should have orderLineItems");
        }
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
        this.orderLineItems = orderLineItems;
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
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
        return Collections.unmodifiableList(orderLineItems);
    }

    public void addOrderLineItem(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
        orderLineItem.setOrder(this);
    }

    public void changeStatus(OrderStatus status) {
        if (orderStatus == status) {
            throw new IllegalArgumentException("Invalid OrderStatus > " + this.orderStatus + " to " + status);
        }
        orderStatus = status;
    }
}
