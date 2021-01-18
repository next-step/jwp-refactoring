package kitchenpos.order.dto;

public class OrderLineRequest {
    private long menuId;
    private long quantity;

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

