package kitchenpos.order.dto;

public class OrderMenuRequest {
    private Long menuId;
    private Long quantity;

    public OrderMenuRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderMenuRequest() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
