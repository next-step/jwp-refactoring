package kitchenpos.menu.dto;

import kitchenpos.global.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity().value());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Quantity toQuantity() {
        return Quantity.of(this.quantity);
    }

    public MenuProduct toMenuProduct() {
        return MenuProduct.of(this.productId, this.quantity);
    }
}
