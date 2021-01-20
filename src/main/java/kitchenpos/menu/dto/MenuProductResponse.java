package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private Long id;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse(final MenuProduct menuProduct) {
        this.id = menuProduct.getId();
        this.product = new ProductResponse(menuProduct.getProduct());
        this.quantity = menuProduct.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
