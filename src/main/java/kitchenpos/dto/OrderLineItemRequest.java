package kitchenpos.dto;

public class OrderLineItemRequest {

    private Long menuId;
    private Long quantity;

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
