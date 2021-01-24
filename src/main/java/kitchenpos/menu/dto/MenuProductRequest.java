package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductRequest;

public class MenuProductRequest {
    private ProductRequest productRequest;
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(ProductRequest productRequest, long quantity) {
        this.productRequest = productRequest;
        this.quantity = quantity;
    }

    public ProductRequest getProductRequest() {
        return productRequest;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return MenuProduct.of(productRequest.toProduct(), quantity);
    }
}
