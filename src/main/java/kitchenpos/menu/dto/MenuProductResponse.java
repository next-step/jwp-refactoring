package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private  long productId;
    private  Long quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getProduct().getId(),
            menuProduct.getQuantity()
        );
    }

    public long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
