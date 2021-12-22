package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;

import java.util.List;

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
        orderLineItems.getOrderLineItems().stream().map(OrderLineItemResponse::from);
    }

    private static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
