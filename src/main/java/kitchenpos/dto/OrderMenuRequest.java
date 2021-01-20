package kitchenpos.dto;

public class OrderMenuRequest {
    private long menuId;
    private long quantity;

    protected OrderMenuRequest(){}

    public OrderMenuRequest(long menuId, long quantity) {
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
