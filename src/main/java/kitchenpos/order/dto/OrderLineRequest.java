package kitchenpos.order.dto;

public class OrderLineRequest {
    private long menuId;
    private int quantity;

    public OrderLineRequest() {
    }

    public OrderLineRequest(long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }
}

