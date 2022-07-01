package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private int quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(final Long seq, final Long productId, final int quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity().value());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
