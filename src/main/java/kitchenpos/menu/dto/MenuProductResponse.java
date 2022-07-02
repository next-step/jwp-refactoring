package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
