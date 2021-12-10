package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Quantity;

public final class MenuProductRequest {

    private final long productId;
    private final int quantity;

    @JsonCreator
    public MenuProductRequest(
        @JsonProperty("productId") long productId,
        @JsonProperty("quantity") int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Quantity quantity() {
        return Quantity.from(quantity);
    }
}
