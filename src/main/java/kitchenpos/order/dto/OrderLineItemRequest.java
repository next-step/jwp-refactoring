package kitchenpos.order.dto;

public class OrderLineItemRequest {
    private long menuId;
    private long quantity;

    protected OrderLineItemRequest() {
    }

    private OrderLineItemRequest(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemRequest of(long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
