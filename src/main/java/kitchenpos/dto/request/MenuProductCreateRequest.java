package kitchenpos.dto.request;

import javax.persistence.Transient;

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
