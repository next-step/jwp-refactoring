package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.table.OrderTable;

public class Order {

    private Long id;
    private Long orderTableId;

    @ManyToOne
    @JoinColumn(
        name = "order_table_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_TO_ORDER_TABLE"),
        nullable = false
    )
    private OrderTable orderTable;

    private String orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatuses;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems;

    public Order() {

    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable, orderLineItems);
        this.orderTable = orderTable;
        this.orderStatuses = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    private void validateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTableIsEmpty(orderTable);
        validateOrderLineItemIsEmpty(orderLineItems);
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있는 경우 주문을 생성할 수 없습니다.");
        }
    }

    private void validateOrderLineItemIsEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 없는 경우 주문을 생성할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
