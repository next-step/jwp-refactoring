package kitchenpos.dto.response;

import kitchenpos.domain.menu.MenuProduct;

import java.util.Objects;

public class MenuProductViewResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;


    public static MenuProductViewResponse of(MenuProduct menuProduct) {
        return new MenuProductViewResponse(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity().toLong()
        );
    }

    public MenuProductViewResponse(Long seq, Long menuId, Long productId, long quantity) {
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductViewResponse that = (MenuProductViewResponse) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }
}
