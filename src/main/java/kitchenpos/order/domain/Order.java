package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
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
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.order.exception.CannotOrderException;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    public static final String CANNOT_ORDER_AN_EMPTY_TABLE = "빈 테이블은 주문할 수 없습니다.";
    public static final String ORDER_ITEM_NOT_FOUND = "주문 항목이 없습니다.";
    public static final String YOU_CANNOT_CHANGE_A_CALCULATED_ORDER = "주문상태가 계산완료인 주문은 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_orders_order_table"), nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, new OrderTable(orderTableId), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this(null, new OrderTable(orderTableId), orderStatus, orderedTime, orderLineItems);
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validationOrderLineItems(orderLineItems);
        validationOrderTable(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    private void validationOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new CannotOrderException(CANNOT_ORDER_AN_EMPTY_TABLE);
        }
    }

    private void validationOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new CannotOrderException(ORDER_ITEM_NOT_FOUND);
        }
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

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (orderStatus.equals(OrderStatus.COMPLETION)) {
            throw new CannotChangeOrderStatusException(YOU_CANNOT_CHANGE_A_CALCULATED_ORDER);
        }
        this.orderStatus = orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public void toOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }
}
