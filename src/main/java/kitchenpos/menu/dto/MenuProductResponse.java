package kitchenpos.menu.dto;

import kitchenpos.common.domian.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse productResponse;
    private Long quantity;

    public MenuProductResponse() {}

    private MenuProductResponse(Long seq, ProductResponse productResponse, Long quantity) {
        this.seq = seq;
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(Long seq, ProductResponse productResponse, Long quantity) {
        return new MenuProductResponse(seq, productResponse, quantity);
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        Quantity quantity = menuProduct.getQuantity();
        ProductResponse response = menuProduct.getProduct().toResponse();
        return new MenuProductResponse(menuProduct.getSeq(), response, quantity.amount());
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
