package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {
    private Long productId;
    private String name;
    private long quantity;

    private MenuProductResponse() {
    }

    public MenuProductResponse(MenuProduct menuProduct) {
        this(menuProduct.getProduct().getId(), menuProduct.getProduct().getName(), menuProduct.getQuantity());
    }

    public MenuProductResponse(Long productId, String name, long quantity) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public long getQuantity() {
        return quantity;
    }
}
