package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private final LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
        this.orderStatus = COOKING;
        this.orderedTime = LocalDateTime.now();
    }

    public Long id() {
        return id;
    }

    public long orderTableId() {
        return orderTable.id();
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public void addOrderTable(final OrderTable orderTable) {
        validateNotEmpty(orderTable);
        this.orderTable = orderTable;
    }

    private void validateNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문테이블이 비어있으면 안됩니다.");
        }
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public void addOrderLineItems(final OrderLineItems orderLineItems, int size) {
        validateOrderLineItemsSize(orderLineItems, size);
        orderLineItems.addOrder(this);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemsSize(OrderLineItems orderLineItems, int size) {
        if (orderLineItems.isNotEqualSize(size)) {
            throw new IllegalArgumentException("비교하는 수와 주문 항목의 수가 일치하지 않습니다.");
        }
    }

    public List<OrderLineItem> readOnlyOrderLineItems() {
        return orderLineItems.readOnlyOrderLineItems();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
