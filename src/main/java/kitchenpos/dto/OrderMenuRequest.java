package kitchenpos.dto;

import javax.validation.constraints.Min;

public class OrderMenuRequest {
    private long menuId;
    @Min(1)
    private long quantity;

    protected OrderMenuRequest(){}

    public OrderMenuRequest(long menuId, long quantity) {
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
