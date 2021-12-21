package kitchenpos.dto.order;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequests {

    private List<OrderLineItemRequest> orderLineItems;

    public OrderLineItemRequests() {
    }

    public OrderLineItemRequests(
        List<OrderLineItemRequest> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    public List<OrderLineItem> toOrderLineItems(List<Menu> menus) {
        return orderLineItems.stream()
            .map(orderLineItemRequest -> orderLineItemRequest.toOrderLineItem(menus))
            .collect(Collectors.toList());
    }
}
