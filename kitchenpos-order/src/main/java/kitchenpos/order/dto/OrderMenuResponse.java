package kitchenpos.order.dto;

public class OrderMenuResponse {
    private long id;
    private long menuId;
    private long quantity;

    protected OrderMenuResponse(){}

    public OrderMenuResponse(long id, long menuId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
