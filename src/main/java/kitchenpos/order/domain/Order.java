package kitchenpos.order.domain;

import kitchenpos.common.Exception.EmptyException;
import kitchenpos.common.Exception.UnchangeableException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(OrderTable orderTable, OrderLineItems orderLineItems) {
        orderTableVaildCheck(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        updateOrderItems(orderLineItems);
    }

    private void orderTableVaildCheck(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
                throw new EmptyException("빈 테이블은 주문 할 수 없습니다.");
        }
    }


    private void updateOrderItems(OrderLineItems orderLineItems) {
        orderLineItems.updateOrder(this);
        this.orderLineItems = orderLineItems;
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
        return orderLineItems.values();
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void updateStatus(String orderStatusRequest) {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new UnchangeableException("이미 완료된 주문입니다.");
        }
        this.orderStatus = OrderStatus.valueOf(orderStatusRequest);
    }
}
