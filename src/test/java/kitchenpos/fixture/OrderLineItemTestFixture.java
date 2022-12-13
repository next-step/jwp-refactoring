package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemTestFixture {

    public static OrderLineItemRequest createOrderLineItemRequest(Long menuId, long quantity) {
        return OrderLineItemRequest.of(menuId, quantity);
    }

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

    public static List<OrderLineItemRequest> mapToRequest(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public static List<OrderLineItem> mapToEntity(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }
}
