package dto.menu;

import kitchenpos.domain.menu.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long quantity;

    MenuProductResponse() {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(seq, that.seq) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
                "seq=" + seq +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
