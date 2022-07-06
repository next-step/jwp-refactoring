package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        this(id, orderTable, OrderStatus.COOKING, orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        validateOrderTable(orderTable);
        validateOrderLineItemRequestEmpty(orderLineItems);

        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItemList) {
        OrderLineItems orderLineItems = convertToOrderLineItems(orderLineItemList);

        validateOrderTable(orderTable);
        validateOrderLineItemRequestEmpty(orderLineItems);

        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderLineItems = orderLineItems;
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

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public void updateOrderStatus(String status) {
        if(Objects.equals(OrderStatus.COMPLETION.name(), orderStatus.name())) {
            throw new IllegalArgumentException();
        }

        orderStatus = OrderStatus.valueOf(status);
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItemRequestEmpty(OrderLineItems orderLineItems) {
        if(orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private OrderLineItems convertToOrderLineItems(List<OrderLineItem> orderLineItems) {
        for(OrderLineItem orderLineitem: orderLineItems) {
            orderLineitem.updateOrder(this);
         }
        return new OrderLineItems(orderLineItems);
    }
}
