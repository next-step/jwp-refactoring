package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import javax.validation.constraints.NotNull;

public class MenuProductRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
