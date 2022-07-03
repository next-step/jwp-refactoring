package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductRequest {

    private Long productId;
    private int quantity;

    public MenuProductRequest() {

    }

    public MenuProductRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct(Menu menu, Product product) {
        return new MenuProduct(menu, product, quantity);
    }

    public MenuProduct toMenuProduct(Product product){
        return new MenuProduct(product, quantity);
    }
}
