package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTableEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private OrderTableEntity orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected OrderEntity() {
    }

    public OrderEntity(Long id, OrderTableEntity orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderEntity(OrderTableEntity orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderEntity(OrderTableEntity orderTable, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        updateOrderItems(orderLineItems);
    }

    private void updateOrderItems(OrderLineItems orderLineItems) {
        orderLineItems.updateOrder(this);
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTableEntity getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemEntity> getOrderLineItems() {
        return orderLineItems.values();
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
