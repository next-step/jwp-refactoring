package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.menu.domain.Quantity;

public class OrderLineItemRequest {
    private Long menuId;
    private Quantity quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
