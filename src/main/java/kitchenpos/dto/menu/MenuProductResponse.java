package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.product.ProductResponse;

public class MenuProductResponse {
    private ProductResponse productResponse;
    private Long quantity;

    protected MenuProductResponse() {
    }

    private MenuProductResponse(final ProductResponse productResponse, final Long quantity) {
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        final ProductResponse productResponse = ProductResponse.from(menuProduct.getProduct());
        return new MenuProductResponse(productResponse, menuProduct.getQuantity().toLong());
    }

    public static MenuProductResponse of(final ProductResponse productResponse, final Long quantity) {
        return new MenuProductResponse(productResponse, quantity);
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Long getQuantity() {
        return quantity;
    }
}
