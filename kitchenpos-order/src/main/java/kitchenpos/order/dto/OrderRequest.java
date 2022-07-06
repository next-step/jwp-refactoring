package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity(List<OrderMenu> orderMenus) {
        List<OrderLineItem> items = orderLineItems.stream()
                .map(orderLineItemRequest -> orderLineItemRequest.toEntity(orderMenus))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        return new Order(orderTableId, items);
    }
}
