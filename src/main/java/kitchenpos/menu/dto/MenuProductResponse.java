package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private final Long id;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(Long id, Long menuId, Long productId, Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getId(),
            menuProduct.getMenu().getId(),
            menuProduct.getProductId(),
            menuProduct.getQuantity()
        );
    }

}
