package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class  MenuProductResponse {
    private Long seq;
    private long quantity;
    private Long productId;

    protected MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, long quantity, Long productId) {
        this.seq = seq;
        this.quantity = quantity;
        this.productId = productId;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuProductQuantity().toLong(), menuProduct.getProductId());
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return this.productId;
    }
}
