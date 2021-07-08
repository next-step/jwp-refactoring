package kitchenpos.dto.request;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.MenuProductCreate;

public class MenuProductCreateRequest {
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductCreate toCreate() {
        return new MenuProductCreate(menuId, productId, new Quantity(quantity));
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
