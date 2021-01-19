package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private long quantity;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(Long seq, long quantity) {
        this.seq = seq;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem it) {
        return new OrderLineItemResponse(it.getSeq(), it.getQuantity());
    }
}
