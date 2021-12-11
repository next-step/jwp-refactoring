package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Quantity;
import kitchenpos.order.domain.OrderLineItem;

public final class OrderLineItemRequest {

    private final long menuId;
    private final long quantity;

    @JsonCreator
    public OrderLineItemRequest(
        @JsonProperty("menuId") long menuId,
        @JsonProperty("quantity") long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Quantity quantity() {
        return Quantity.from(quantity);
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.of(quantity, menuId);
    }
}
