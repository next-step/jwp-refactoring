package kitchenpos.order.ui.response;

import kitchenpos.domain.OrderLineItem;

public final class OrderLineItemResponse {

    private final long seq;
    private final long menuId;
    private final long quantity;

    private OrderLineItemResponse(long seq, long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return null;
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
