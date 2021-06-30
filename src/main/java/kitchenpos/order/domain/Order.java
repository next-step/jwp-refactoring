package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public Order(List<OrderLineItem> orderLineItems) {
        this.orderStatus = OrderStatus.COOKING;
        orderLineItems.forEach(this::addOrderLineItem);
    }

    public void addOrderLineItem(OrderLineItem item) {
        orderLineItems.add(item);
        item.relatedTo(this);
    }

    public void changeOrderStatus(String orderStatus) {
        changeOrderStatus(OrderStatus.valueOf(orderStatus));
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletedOrder()) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus;
    }

    public void group(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public boolean isCompletedOrder() {
        return orderStatus.isCompletedOrder();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id)
            && Objects.equals(orderTable, order.orderTable)
            && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus);
    }
}
