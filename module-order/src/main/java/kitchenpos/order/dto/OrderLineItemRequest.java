package kitchenpos.order.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
