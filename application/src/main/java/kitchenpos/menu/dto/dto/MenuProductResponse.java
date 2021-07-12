package kitchenpos.menu.dto.dto;

import java.util.Objects;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuProductResponse {
    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProductResponse() { }

    public MenuProductResponse(Long menuId, Long productId, Long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProductDto menuProduct) {
        return new MenuProductResponse(menuProduct.getMenuId(),
                                       menuProduct.getProductId(),
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
        MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(productId,
                                                                     that.productId)
            && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, productId, quantity);
    }
}
