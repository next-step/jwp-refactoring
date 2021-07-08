package kitchenpos.order.dto;

import kitchenpos.menu.domain.Quantity;

public class OrderLineItemRequest {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private Quantity quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long seq, Long orderId, Long menuId, Quantity quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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

    public Quantity getQuantity() {
        return quantity;
    }

}
