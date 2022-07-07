package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long menuId;
    private int quantity;

    protected MenuProductResponse() {
    }

    private MenuProductResponse(Long seq, Long productId, Long menuId, int quantity) {
        this.seq = seq;
        this.productId = productId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProductId(),
                menuProduct.getMenu().getId(), menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return getQuantity() == that.getQuantity()
                && Objects.equals(getSeq(), that.getSeq())
                && Objects.equals(getProductId(), that.getProductId())
                && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq(), getProductId(), menuId, getQuantity());
    }
}
