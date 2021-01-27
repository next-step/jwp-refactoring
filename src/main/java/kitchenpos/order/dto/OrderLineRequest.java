package kitchenpos.order.dto;

public class OrderLineRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineRequest() {
    }

    public OrderLineRequest(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
