package kitchenpos.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import kitchenpos.domain.OrderStatus;

import java.util.List;

public class OrderRequest {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long orderTableId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus orderStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    private OrderRequest(final Long orderTableId, final OrderStatus orderStatus, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, null, orderLineItems);
    }

    public static OrderRequest of(final Long orderTableId, final String orderStatus) {
        return new OrderRequest(orderTableId, OrderStatus.valueOf(orderStatus), null);
    }

    public static OrderRequest from(final OrderStatus orderStatus) {
        return new OrderRequest(null, orderStatus, null);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
