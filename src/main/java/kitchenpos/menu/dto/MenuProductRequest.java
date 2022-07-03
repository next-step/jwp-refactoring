package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Integer quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(Long seq, Long menuId, Long productId, Integer quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct of() {
        return new MenuProduct(this.seq, new Menu(menuId), productId, quantity);
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

    public Integer getQuantity() {
        return quantity;
    }
}
