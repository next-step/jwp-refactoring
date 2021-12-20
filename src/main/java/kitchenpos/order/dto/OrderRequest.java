package kitchenpos.order.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public void setOrderLineItemRequests(List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public List<OrderLineItem> toOrderLineItem(Order order, Menu menu) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> orderLineItemRequest.toOrderLineItem(order, menu))
                .collect(Collectors.toList());
    }

}
