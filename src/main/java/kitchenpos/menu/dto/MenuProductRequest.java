package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct() {
        return MenuProduct.of(productId, Quantity.of(quantity));
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
