package kitchenpos.order.dto;

public class OrderLineItemRequest {
    private long menuId;
    private long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(long menuId, long quantity) {
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
