package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductRequest() {

    }

    public MenuProductRequest(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getMenuId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
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
