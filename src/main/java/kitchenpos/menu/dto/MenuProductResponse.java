package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    public Long seq;
    public Long productId;
    public Long quantity;

    public MenuProductResponse(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
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

    public Long getQuantity() {
        return quantity;
    }
}
