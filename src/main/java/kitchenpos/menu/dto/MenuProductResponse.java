package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class  MenuProductResponse {
    private Long seq;
    private long quantity;
    private ProductResponse productResponse;

    protected MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, long quantity, ProductResponse productResponse) {
        this.seq = seq;
        this.quantity = quantity;
        this.productResponse = productResponse;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getQuantity(), ProductResponse.of(menuProduct.getProduct()));
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }
}
