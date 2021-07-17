package kitchenpos.menu.dto;

import kitchenpos.product.dto.ProductResponse;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductReponse {
    private long id;
    private ProductResponse productResponse;
    private long quantity;

    public MenuProductReponse() {
    }

    public MenuProductReponse(long id, ProductResponse productResponse, long quantity) {
        this.id = id;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductReponse of(MenuProduct menuProduct) {
        return new MenuProductReponse(menuProduct.getId(),
                ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity());
    }

    public long getId() {
        return id;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
