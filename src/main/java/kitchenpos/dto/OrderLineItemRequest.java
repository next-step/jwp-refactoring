package kitchenpos.dto;

public class OrderLineItemRequest {
    private long menuId;
    private long quantity;

    public OrderLineItemRequest(long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
