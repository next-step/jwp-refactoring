package kitchenpos.order.domain;

import kitchenpos.order.application.exception.InvalidOrderState;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = {PERSIST, MERGE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        validateOrderStatus(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        addOrderLineItems(orderLineItems);
    }

    public static Order of(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderStatus, orderLineItems);
    }

    private void validateOrderStatus(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderState("빈 테이블에 주문을 받을 수 없습니다.");
        }
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
    }

    public void setOrderStatus(OrderStatus changeStatus) {
        if (orderStatus.isCompleted()) {
            throw new InvalidOrderState("이미 계산을 완료하였습니다.");
        }
        this.orderStatus = changeStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
