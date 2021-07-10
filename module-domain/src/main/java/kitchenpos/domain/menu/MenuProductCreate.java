package kitchenpos.domain.menu;

import kitchenpos.domain.Quantity;

public class MenuProductCreate {
    private Long menuId;
    private Long productId;
    private Quantity quantity;

    public MenuProductCreate(Long menuId, Long productId, long quantity) {
        this(menuId, productId, new Quantity(quantity));
    }

    public MenuProductCreate(Long menuId, Long productId, Quantity quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

}
