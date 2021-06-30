package kitchenpos.dto.response;

import kitchenpos.domain.MenuProduct;

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
                menuProduct.getQuantity()
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
}
