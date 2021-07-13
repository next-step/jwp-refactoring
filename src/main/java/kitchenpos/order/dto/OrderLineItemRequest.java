package kitchenpos.order.dto;

public class OrderLineItemRequest {
    public Long menuId;
    public Long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return this.menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
