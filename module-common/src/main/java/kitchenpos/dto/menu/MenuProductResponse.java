package kitchenpos.dto.menu;

import java.util.Objects;
import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {

    private final Long id;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    public MenuProductResponse(Long id, Long menuId, Long productId, Long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getId(),
            menuProduct.getMenu().getId(),
            menuProduct.getProductId(),
            menuProduct.getQuantity()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(menuId, that.menuId)
            && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menuId, productId, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
            "id=" + id +
            ", menuId=" + menuId +
            ", productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }

}
