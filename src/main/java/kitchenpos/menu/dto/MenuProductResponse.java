package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq()
                , menuProduct.getProductId()
                , menuProduct.getQuantity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(seq, that.seq)
                && Objects.equals(productId, that.productId)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
