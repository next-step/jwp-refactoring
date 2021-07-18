package kitchenpos.order.domain;

import kitchenpos.exception.CannotUpdateException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static kitchenpos.common.Message.*;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    public Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus) {
        this(null, orderTableId, orderStatus);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus) {
        validateOrderTableStatus(orderTableId);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    private void validateOrderTableStatus(Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException(ERROR_ORDER_SHOULD_HAVE_NON_EMPTY_TABLE.showText());
        }
    }

    public Order changeOrderStatus(OrderStatus orderStatus) {
        if (checkWhetherCompleted()) {
            throw new CannotUpdateException(ERROR_ORDER_STATUS_CANNOT_BE_CHANGED_WHEN_COMPLETED);
        }
        this.orderStatus = orderStatus;
        return this;
    }

    public boolean cannotBeChanged() {
        return orderStatus == MEAL || orderStatus == COOKING;
    }

    private boolean checkWhetherCompleted() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id)
                && Objects.equals(orderTableId, order.orderTableId)
                && orderStatus == order.orderStatus
                && Objects.equals(orderedTime, order.orderedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderedTime);
    }
}
