package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.common.domain.Quantity;
import kitchenpos.domain.MenuProduct;

public final class MenuProductRequest {

    private final long productId;
    private final long quantity;

    @JsonCreator
    public MenuProductRequest(
        @JsonProperty("productId") long productId,
        @JsonProperty("quantity") long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Quantity quantity() {
        return Quantity.from(quantity);
    }

    public MenuProduct toEntity() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
