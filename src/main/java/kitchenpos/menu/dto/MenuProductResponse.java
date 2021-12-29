package kitchenpos.menu.dto;

import java.util.Objects;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private long quantity;

    private MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
            menuProduct.getProduct().getId(),
            menuProduct.getQuantity());
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MenuProductResponse))
            return false;
        MenuProductResponse that = (MenuProductResponse)o;
        return quantity == that.quantity &&
            Objects.equals(seq, that.seq) &&
            Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
