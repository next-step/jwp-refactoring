package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    private Long productId;

    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProduct toEntity() {
        return MenuProduct.of(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
