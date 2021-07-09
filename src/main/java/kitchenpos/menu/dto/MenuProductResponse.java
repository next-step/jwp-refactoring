package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private ProductResponse product;
    private long quantity;

    private MenuProductResponse(Product product, long quantity) {
        this.product = ProductResponse.of(product);
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProduct(), menuProduct.getQuantity());
    }
}
