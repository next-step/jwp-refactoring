package kitchenpos.order.dto;

public class OrderLineRequest {
    private Long menuId;
    private long quantity;

    public OrderLineRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineRequest() {
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
