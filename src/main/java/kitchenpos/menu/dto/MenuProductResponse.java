package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    public static MenuProductResponse from(MenuProduct menuProduct) {
        MenuProductResponse response = new MenuProductResponse();

        response.seq = menuProduct.getSeq();
        response.menuId = menuProduct.getMenu().getId();
        response.productId = menuProduct.getProductId();
        response.quantity = menuProduct.getQuantity().getValue();

        return response;
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
}
