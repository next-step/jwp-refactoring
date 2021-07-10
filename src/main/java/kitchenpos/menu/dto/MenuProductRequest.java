package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductRequest {
    private Long productId;
    private int quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public MenuProduct toEntityWith(Product product) {
        return new MenuProduct(product, quantity);
    }
}
