package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse() {
        // empty
    }

    private OrderLineItemResponse(final Long orderId, final OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderId;
        this.menuId = orderLineItem.menuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public static OrderLineItemResponse of(final Long orderId, final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderId, orderLineItem);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
