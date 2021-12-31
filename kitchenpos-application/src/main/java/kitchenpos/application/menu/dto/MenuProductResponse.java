package kitchenpos.application.menu.dto;

import kitchenpos.core.menu.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private final Long seq;
    private final Long productId;
    private final long quantity;

    public MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
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

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        MenuProductResponse that = (MenuProductResponse) target;

        if (quantity != that.quantity) return false;
        if (!Objects.equals(seq, that.seq)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
