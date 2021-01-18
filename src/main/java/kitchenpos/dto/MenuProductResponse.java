package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(), menuProduct.getQuantity());
    }

    private MenuProductResponse(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
