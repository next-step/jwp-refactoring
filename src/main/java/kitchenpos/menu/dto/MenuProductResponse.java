package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    private MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return getQuantity() == that.getQuantity()
                && Objects.equals(getMenuId(), that.getMenuId())
                && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMenuId(), getProductId(), getQuantity());
    }
}
