package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(Long menuId, Long productId, Long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getMenu().getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity().value());
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
}
