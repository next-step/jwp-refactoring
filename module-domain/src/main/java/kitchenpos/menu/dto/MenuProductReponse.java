package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductReponse {
    private long seq;
    private ProductResponse productResponse;
    private long quantity;

    public MenuProductReponse() {
    }

    public MenuProductReponse(long seq, ProductResponse productResponse, long quantity) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductReponse of(MenuProduct menuProduct) {
        return new MenuProductReponse(menuProduct.getSeq(),
                ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity());
    }

    public long getSeq() {
        return seq;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
