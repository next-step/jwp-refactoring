package kitchenpos.dto.request;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.order.OrderLineItemCreate;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private long quantity;

    public OrderLineItemCreateRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItemCreate toCreate() {
        return new OrderLineItemCreate(menuId, new Quantity(quantity));
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
