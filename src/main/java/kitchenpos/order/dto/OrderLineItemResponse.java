package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class OrderLineItemResponse {

    private Long id;
    private Long menuId;
    private Long quantity;

    private OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.id = orderLineItem.getSeq();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem);
    }

    public static List<OrderLineItemResponse> ofList(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(toList());
    }
}
