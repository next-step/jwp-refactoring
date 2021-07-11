package kitchenpos.order.domain;

import static kitchenpos.exception.KitchenposExceptionMessage.ALREADY_COMPLETION_ORDER;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.exception.KitchenposException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    @CreatedDate
    private LocalDateTime orderedTime;

    protected Order() {
        // empty
    }

    public Order(final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final List<OrderLineItem> orderLineItems) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems.add(orderLineItems);
        this.orderedTime = LocalDateTime.now();

        this.orderLineItems.connectOrder(this);
    }

    public Order changeOrderStatus(final OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new KitchenposException(ALREADY_COMPLETION_ORDER);
        }
        this.orderStatus = orderStatus;
        return this;
    }

    private boolean isCompletion() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        if (Objects.isNull(this.orderStatus)) {
            return OrderStatus.NONE;
        }
        return this.orderStatus;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
