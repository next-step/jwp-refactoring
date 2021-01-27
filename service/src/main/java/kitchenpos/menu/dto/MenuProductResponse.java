package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;

import java.util.Objects;

public class MenuProductResponse {
    private Long id;
    private Long menuId;
    private Long productId;
    private Long quantity;

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getId(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    private MenuProductResponse(Long id, Long menuId, Long productId, Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductResponse() {
    }

    public Long getId() {
        return id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(menuId, that.menuId) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
                "seq=" + id +
                ", menuId=" + menuId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
