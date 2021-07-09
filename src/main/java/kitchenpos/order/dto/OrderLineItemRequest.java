package kitchenpos.order.dto;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {}

    public OrderLineItemRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItemRequest findByMenuId(Long menuId) {
        if (this.menuId.equals(menuId)) {
            return this;
        }
        return null;
    }
}
