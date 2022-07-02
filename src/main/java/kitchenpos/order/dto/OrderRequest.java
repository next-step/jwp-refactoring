package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.Exception.EmptyOrderLineItemsException;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    protected OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        validateNullOrderLineItem(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    private void validateNullOrderLineItem(List<OrderLineItemRequest> orderLineItems) {
        if (orderLineItems == null) {
            throw new EmptyOrderLineItemsException("주문 항목 목록이 있어야 합니다.");
        }
    }
}
