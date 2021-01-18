package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {
    private Long id;
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long id, final Long menuId, final Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public OrderLineItemRequest setId(final Long id) {
        this.id = id;
        return this;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }

    public OrderLineItem toOrderLineItem() {
        return OrderLineItem.of(this.getMenuId(), this.getQuantity());
    }
}
