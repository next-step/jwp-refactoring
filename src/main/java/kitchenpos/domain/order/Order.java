package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "orders")
public class Order {

    public static final String EMPTY_ORDER_TABLE_ERROR_MESSAGE = "주문 테이블이 비어있는 경우 주문을 생성할 수 없습니다.";
    public static final String EMPTY_ORDER_LINE_ITEM_ERROR_MESSAGE = "주문 항목이 없는 경우 주문을 생성할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
        name = "order_table_id",
        foreignKey = @ForeignKey(name = "FK_ORDER_TO_ORDER_TABLE"),
        nullable = false
    )
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    // TODO 일급 컬렉션
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    public Order() {

    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrder(orderTable, orderLineItems);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
        orderLineItems.forEach(it -> it.setOrder(this));
    }

    private void validateOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTableIsEmpty(orderTable);
        validateOrderLineItemIsEmpty(orderLineItems);
    }

    private void validateOrderTableIsEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_TABLE_ERROR_MESSAGE);
        }
    }

    private void validateOrderLineItemIsEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ORDER_LINE_ITEM_ERROR_MESSAGE);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
