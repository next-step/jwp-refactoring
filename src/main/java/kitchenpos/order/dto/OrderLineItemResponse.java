package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    private OrderLineItemResponse() {}

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getId(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity());
    }

}
