package kitchenpos.service.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(OrderLineItem orderLineItem) {
        this.seq = orderLineItem.getSeq();
        this.orderId = orderLineItem.getOrderId();
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
