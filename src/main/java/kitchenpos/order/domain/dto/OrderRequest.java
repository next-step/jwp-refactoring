package kitchenpos.order.domain.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COOKING;

public class OrderRequest {

    private Long orderTableId;

    @NotEmpty
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderRequest of(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }

    public Order toEntity(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, COOKING, orderLineItems);
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
