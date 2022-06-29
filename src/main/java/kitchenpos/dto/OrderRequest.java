package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

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

    public Order toOrder(OrderTable orderTable) {
        return new Order(orderTable);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    private void validateNullOrderLineItem(List<OrderLineItemRequest> orderLineItems) {
        if (orderLineItems == null) {
            throw new IllegalArgumentException("주문 항목 목록이 있어야 합니다.");
        }
    }
}
