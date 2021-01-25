package kitchenpos.dto.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public MenuProductRequest(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
                "seq=" + seq +
                ", menuId=" + menuId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
