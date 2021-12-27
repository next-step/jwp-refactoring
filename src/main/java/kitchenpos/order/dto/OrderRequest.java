package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.MenuId;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public OrderRequest() {
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public int getOrderItemSize() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> toOrderItems() {
        return orderLineItems.stream()
            .map(s -> new OrderLineItem(null, new MenuId(s.getMenuId()), s.getQuantity()))
            .collect(Collectors.toList());
    }

    public List<Long> toMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineRequest::getMenuId)
            .collect(Collectors.toList());
    }
}
