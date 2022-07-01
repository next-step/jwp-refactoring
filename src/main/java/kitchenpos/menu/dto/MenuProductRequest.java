package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    protected MenuProductRequest() {}

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct of(Menu menu, Product product) {
        return new MenuProduct(menu, product, new Quantity(quantity));
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
