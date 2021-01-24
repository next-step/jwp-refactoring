package kitchenpos.order.dto;

import kitchenpos.domain.Quantity;

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

    public Quantity ofQuantity() {
        return Quantity.of(quantity);
    }
}

