package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(final Long seq, final Long productId, final Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
