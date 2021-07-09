package kitchenpos.dto;

import kitchenpos.domain.Quantity;

public class OrderLineItemRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Quantity quantity() {
        return Quantity.valueOf(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
