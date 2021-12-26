package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
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
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(final OrderTable orderTable) {
        validateOrderTableNotEmpty(orderTable);

        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
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

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatusNotCompletion();

        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);

        orderLineItems.forEach(orderLineItem -> this.orderLineItems.addOrderLineItem(orderLineItem));
    }

    public boolean isCompleteStatus() {
        return orderStatus.isCompletion();
    }

    private void validateOrderStatusNotCompletion() {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException("계산 완료된 주문입니다.");
        }
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않습니다.");
        }
    }

    private void validateOrderLineItemsNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 하나 이상이어야 합니다.");
        }
    }
}
