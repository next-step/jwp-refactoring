package kitchenpos.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, long quantity) {
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
