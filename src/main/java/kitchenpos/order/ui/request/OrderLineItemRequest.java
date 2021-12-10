package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Quantity;

public final class OrderLineItemRequest {

    private final long menuId;
    private final int quantity;

    @JsonCreator
    public OrderLineItemRequest(
        @JsonProperty("menuId") long menuId,
        @JsonProperty("quantity") int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public long menuId() {
        return menuId;
    }

    public Quantity quantity() {
        return Quantity.from(quantity);
    }
}
