package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.Product;

public class MenuProductDto {

    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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

    public MenuProduct toMenuProduct(Product product){
        return new MenuProduct(product, quantity);
    }
}
