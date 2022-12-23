package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderMenuId;
    private final long quantity;

    private OrderLineItemResponse(Long seq, Long orderMenuId, long quantity) {
        this.seq = seq;
        this.orderMenuId = orderMenuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
            orderLineItem.getSeq(), orderLineItem.getOrderMenuId(), orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderMenuId() {
        return orderMenuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
