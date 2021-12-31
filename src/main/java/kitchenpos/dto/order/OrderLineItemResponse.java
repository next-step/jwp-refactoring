package kitchenpos.dto.order;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long menuId;
    private long quantity;

    protected OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity().toLong());
    }

    public static OrderLineItemResponse of(final Long seq, final Long menuId, final long quantity) {
        return new OrderLineItemResponse(seq, menuId, quantity);
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
