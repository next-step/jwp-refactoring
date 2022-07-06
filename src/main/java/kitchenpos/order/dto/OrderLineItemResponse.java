package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private Long orderId;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, Long menuId, Long orderId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(),
            orderLineItem.getOrder().getId(), orderLineItem.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
