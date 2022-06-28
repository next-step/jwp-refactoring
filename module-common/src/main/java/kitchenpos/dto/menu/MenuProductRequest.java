package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductRequest {

    private final Long productId;
    private final Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

}
