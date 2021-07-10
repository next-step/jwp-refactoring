package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    private String orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private final OrderLineItems orderLineItems = new OrderLineItems();

    public Order() {
    }

    public Order(OrderTable orderTable, String orderStatus, List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems.addAll(orderLineItems);
        this.orderLineItems.updateOrder(this);
    }

    public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        this.orderTable = orderTable;
        this.orderLineItems.addAll(orderLineItems.toList());
        this.orderLineItems.updateOrder(this);
        this.orderStatus = OrderStatus.COOKING.name();
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.toList();
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (Objects.equals(this.orderStatus, OrderStatus.COMPLETION.name())) {
            throw new IllegalArgumentException();
        }

        this.orderStatus = orderStatus.name();
    }

    public boolean isNotCompleted() {
        return !Objects.equals(orderStatus, OrderStatus.COMPLETION.name());
    }
}
