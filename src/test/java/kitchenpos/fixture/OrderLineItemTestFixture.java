package kitchenpos.fixture;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemTestFixture {

    public static OrderLineItemRequest 주문정보요청(Long menuId, long quantity) {
        return OrderLineItemRequest.of(menuId, quantity);
    }

    public static OrderLineItem 주문정보(Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

    public static List<OrderLineItemRequest> 주문정보요청목록(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItemRequest.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public static List<OrderLineItem> 주문정보목록(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> OrderLineItem.of(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }
}
