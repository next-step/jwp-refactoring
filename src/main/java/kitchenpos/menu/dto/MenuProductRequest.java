package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {

    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long menuId, Long productId, Long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(Long productId, Long quantity) {
        this.menuId = null;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public static List<MenuProductRequest> listOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductRequest::of)
            .collect(Collectors.toList());
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

    public MenuProduct toMenuProduct() {
        return new MenuProduct(menuId, productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
            "menuId=" + menuId +
            ", productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }
}
