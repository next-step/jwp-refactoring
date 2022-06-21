package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private int quantity;

    public MenuProductResponse(MenuProduct menuProduct) {
        this.seq = menuProduct.getSeq();
        this.quantity = Math.toIntExact(menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public int getQuantity() {
        return quantity;
    }
}
