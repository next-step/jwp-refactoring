package kitchenpos.domain;

public class OrderLineItemCreate {
    private long menuId;
    private long quantity;

    public OrderLineItemCreate(long menuId, long quantity) {
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
