package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

public class MenuProductResponse {
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse() {
    }

    private MenuProductResponse(Product product, long quantity) {
        this.product = ProductResponse.of(product);
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getProduct(), menuProduct.getQuantity());
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
