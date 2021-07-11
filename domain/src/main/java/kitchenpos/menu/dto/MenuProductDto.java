package kitchenpos.menu.dto;

import java.util.Objects;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {
    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductDto() { }

    public MenuProductDto(Long menuId, Long productId, Long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getMenuId(),
                                  menuProduct.getProduct().getId(),
                                  menuProduct.getQuantity());
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProductDto that = (MenuProductDto) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(productId,
                                                                     that.productId)
            && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, productId, quantity);
    }
}
