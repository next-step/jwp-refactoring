package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductRequest(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(toMenuId(menuProduct.getMenu()), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    private static Long toMenuId(Menu menu) {
        if(menu != null) {
            return menu.getId();
        }

        return null;
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
}
