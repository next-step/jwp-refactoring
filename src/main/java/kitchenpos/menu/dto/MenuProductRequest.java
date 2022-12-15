package kitchenpos.menu.dto;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.util.List;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    protected MenuProductRequest() {}

    private MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProductRequest from(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity().value());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct createMenuProduct(Product product) {
        return new MenuProduct(new Quantity(quantity), product);
    }
}
