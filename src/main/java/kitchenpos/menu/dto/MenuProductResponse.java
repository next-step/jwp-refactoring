package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
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

    public static List<MenuProductResponse> listOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductResponse::of)
            .collect(Collectors.toList());
    }

    private static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProductId(), menuProduct.getQuantity().getValue());
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
            "seq=" + seq +
            ", menuId=" + menuId +
            ", productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }
}
