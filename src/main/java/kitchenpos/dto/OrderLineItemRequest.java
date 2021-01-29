package kitchenpos.dto;

public class OrderLineItemRequest {

    private Long orderLineItemId;
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest(Long menuId, Long quantity) {
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
