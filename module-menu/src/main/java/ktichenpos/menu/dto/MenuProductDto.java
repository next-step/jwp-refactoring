package ktichenpos.menu.dto;

import ktichenpos.menu.domain.MenuProduct;

public class MenuProductDto {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductDto() {

    }

    private MenuProductDto(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        if (menuProduct.getMenu() == null) {
            return new MenuProductDto(
                    menuProduct.getSeq()
                    , null
                    , menuProduct.getProductId()
                    , menuProduct.getQuantity()
            );
        }
        return new MenuProductDto(
                menuProduct.getSeq()
                , menuProduct.getMenu().getId()
                , menuProduct.getProductId()
                , menuProduct.getQuantity()
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
}
