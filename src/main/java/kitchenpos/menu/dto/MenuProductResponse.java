package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {

    private ProductResponse productResponse;
    private Long quantity;

    public MenuProductResponse(ProductResponse productResponse, Long quantity) {
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        ProductResponse productResponse = ProductResponse.of(menuProduct
                .getProduct());
        Long quantity = menuProduct.getQuantity()
                .getValue();
        return new MenuProductResponse(productResponse, quantity);
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Long getQuantity() {
        return quantity;
    }
}
