package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(columnDefinition = "bigint(20)")
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        setOrderLineItems(orderLineItems);
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

    public void setOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
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

    public void setOrderLineItems(final OrderLineItems orderLineItems) {
        orderLineItems.setOrder(this);
        this.orderLineItems = orderLineItems;
    }

    public boolean isCompleted() {
        return Objects.equals(OrderStatus.COMPLETION, orderStatus);
    }

    private void ordered() {
        this.orderStatus = OrderStatus.COOKING;
    }

    public void place() {
        validateCreatable();
        ordered();
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateStatusChangeable();
        this.orderStatus = orderStatus;
    }

    private void validateCreatable() {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 등록할 수 없습니다.");
        }
    }

    private void validateStatusChangeable() {
        if (isCompleted()) {
            throw new IllegalArgumentException("이미 완료(COMPLETION) 된 주문입니다.");
        }
    }
}
