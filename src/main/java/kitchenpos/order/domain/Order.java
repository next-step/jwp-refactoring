package kitchenpos.order.domain;

import kitchenpos.exception.CannotUpdateException;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

import static kitchenpos.common.Message.*;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        validateOrderTableStatus(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        orderLineItems.ofOrder(this);
        orderTable.addOrder(this);
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
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

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(final OrderTable orderTable) {
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
}
