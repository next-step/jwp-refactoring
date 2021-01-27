package kitchenpos.dto.order;

public class OrderLineMenuRequest {
    private Long menuId;
    private int quantity;

    public OrderLineMenuRequest() {
    }

    public OrderLineMenuRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}
