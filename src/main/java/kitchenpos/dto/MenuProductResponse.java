package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private final Long seq;
    private final Long productId;
    private final long quantity;

    private MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getSeq(),
            menuProduct.getProductId(),
            menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
