package kitchenpos.menu.dto;

import java.util.Objects;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {
    private Long id;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(MenuProduct menuProduct) {
        this(menuProduct.getId(), menuProduct.getMenuId(),
            menuProduct.getProductId(), menuProduct.getQuantity().getValue());
    }

    public MenuProductResponse(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuProductResponse that = (MenuProductResponse)o;
        return quantity == that.quantity && Objects.equals(id, that.id) && Objects.equals(menuId,
            that.menuId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, productId, quantity);
    }
}
