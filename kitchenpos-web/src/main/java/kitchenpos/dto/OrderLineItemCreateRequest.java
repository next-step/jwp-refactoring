package kitchenpos.dto;

import kitchenpos.domain.model.OrderLineItem;

public class OrderLineItemCreateRequest {

    private Long menuId;

    private long quantity;

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    protected OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
