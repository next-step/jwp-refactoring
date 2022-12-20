package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private Long orderId;
    private Long quantity;

    protected OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, Long menuId, Long orderId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenu().getId(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
