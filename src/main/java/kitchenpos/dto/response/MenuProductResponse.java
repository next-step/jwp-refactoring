package kitchenpos.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct){
        return new MenuProductResponse(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
