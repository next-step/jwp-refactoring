package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private Long quantity;

    public OrderLineItemResponse(Long seq, Long menuId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static List<OrderLineItemResponse> fromList(OrderLineItems orderLineItems) {
        return orderLineItems.getOrderLineItems().stream().map(OrderLineItemResponse::from).collect(Collectors.toList());
    }

    private static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }
}
