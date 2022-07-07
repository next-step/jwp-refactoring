package kitchenpos.order.dto;

public class CreateOrderTableItemRequest {

    private Long menuId;
    private Long quantity;

    public CreateOrderTableItemRequest() {

    }

    public CreateOrderTableItemRequest(Long menuId, Long quantity) {
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
