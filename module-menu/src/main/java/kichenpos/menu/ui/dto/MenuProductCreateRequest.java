package kichenpos.menu.ui.dto;

import kichenpos.menu.domain.MenuProduct;
import kichenpos.menu.domain.Product;

public class MenuProductCreateRequest {
    private Long productId;
    private long quantity;

    private MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Product product) {
        return new MenuProduct(product, quantity);
    }
}
