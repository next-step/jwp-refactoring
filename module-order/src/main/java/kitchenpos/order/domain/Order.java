package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ErrorCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @CreatedDate
    private LocalDateTime orderedTime;
    private Long orderTableId;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(OrderStatus orderStatus, Long orderTableId) {
        this.orderStatus = orderStatus;
        this.orderTableId = orderTableId;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getElements();
    }

    public void register(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderTable(orderTable);
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.orderLineItems.add(this);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.getOrderStatus())) {
            throw new BadRequestException(ErrorCode.CAN_NOT_CHANGE_COMPLETED_ORDER_STATUS);
        }
        this.orderStatus = orderStatus;
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new BadRequestException(ErrorCode.ORDER_LINE_ITEM_EMPTY);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new BadRequestException(ErrorCode.ORDER_TABLE_EMPTY);
        }
    }
}
