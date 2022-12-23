package menu.dto;

import menu.domain.MenuProduct;
import product.domain.Product;

public class MenuProductRequest {

    private long productId;
    private long quantity;

    private MenuProductRequest() {}

    public MenuProductRequest(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(final long productId, final long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
         return MenuProduct.of(product, quantity);
     }
}
