package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.order.domain.ItemQuantity;

public class OrderLineItemRequest {
    private Long menuId;
    private ItemQuantity quantity;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, ItemQuantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public ItemQuantity getQuantity() {
        return quantity;
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
