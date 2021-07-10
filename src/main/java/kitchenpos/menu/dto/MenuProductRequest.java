package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;

public class MenuProductRequest {
    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductRequest() {}

    public MenuProductRequest(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getSeq(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
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
