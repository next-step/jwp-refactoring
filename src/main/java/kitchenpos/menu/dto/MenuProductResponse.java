package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.common.domain.Quantity;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductResponse() {}

    private MenuProductResponse(Long seq, Menu menu, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menu.getId();
        this.productId = productId;
        this.quantity = quantity.value();
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                menuProduct.getMenu(),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
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
