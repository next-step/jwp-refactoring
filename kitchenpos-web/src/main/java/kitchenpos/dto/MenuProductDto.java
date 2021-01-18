package kitchenpos.dto;

import kitchenpos.domain.model.MenuProduct;

public class MenuProductDto {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

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

    public MenuProductDto() {
    }

    public MenuProductDto(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(seq, productId, quantity);
    }

    public static MenuProductDto of(MenuProduct menuProduct, Long menuId) {
        return new MenuProductDto(menuProduct.getId(), menuId, menuProduct.getProductId(), menuProduct.getQuantity());
    }
}
