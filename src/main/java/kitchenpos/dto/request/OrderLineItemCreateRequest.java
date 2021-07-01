package kitchenpos.dto.request;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemCreateRequest(Long menuId, long quantity) {
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
