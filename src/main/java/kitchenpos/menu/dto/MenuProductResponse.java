package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {
    private Product product;
    private long quantity;

    private MenuProductResponse(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProduct(), menuProduct.getQuantity());
    }
}
