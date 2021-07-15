package kitchenpos.order.util;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public Order mapFormToOrder(OrderRequest orderRequest) {
        OrderLineItems orderLineItems = mapFormToOrderLineItems(orderRequest.getOrderLineItemRequests());
        return new Order(orderRequest.getOrderTableId(), orderLineItems);
    }

    private OrderLineItems mapFormToOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemRequest -> toOrderLineItems(orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
        return new OrderLineItems(orderLineItems);
    }

    private OrderLineItem toOrderLineItems(Long menuId, Long quantity) {
        if (menuId == null) {
            throw new IllegalArgumentException("메뉴 ID는 필수 입니다.");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("메뉴 수량은 필수 입니다.");
        }
        return new OrderLineItem(menuId, quantity);
    }
}
