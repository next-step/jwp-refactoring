package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;

public class OrderRequest {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, String orderStatus,
        List<OrderLineItemRequest> orderLineItems) {
        this(null, orderTableId, orderStatus, null, orderLineItems);
    }

    public OrderRequest(String orderStatus) {
        this(null, null, orderStatus, null, Collections.emptyList());
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this(null, orderTableId, null, null, orderLineItems);
    }

    public OrderRequest(Long id, Long orderTableId, String orderStatus,
        LocalDateTime orderedTime, List<OrderLineItemRequest> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(
            id,
            orderTable,
            orderStatus,
            orderedTime,
            orderLineItems
        );
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

    public List<OrderLineItemRequest> getOrderLineItems() {
        if (Objects.isNull(orderLineItems)) {
            return Collections.emptyList();
        }
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
