package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private Long menuId;
    private Long quantity;

    private MenuProductResponse(Long seq, Long productId, Long menuId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getProduct().getId(),
                menuProduct.getMenu().getId(),
                menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static <R> R getProduct(MenuProduct menuProduct) {
        return null;
    }
}
