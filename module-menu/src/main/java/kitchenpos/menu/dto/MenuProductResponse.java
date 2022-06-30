package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                toMenuId(menuProduct.getMenu()),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
    }

    private static Long toMenuId(Menu menu) {
        if (menu != null) {
            return menu.getId();
        }

        return null;
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
        if (!(o instanceof MenuProductResponse)) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        if (Objects.equals(getSeq(), that.getSeq())) return true;
        return getQuantity() == that.getQuantity() && Objects.equals(getMenuId(), that.getMenuId()) && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq(), getMenuId(), getProductId(), getQuantity());
    }
}
