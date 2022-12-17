package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {

    private Long productId;

    private Long quantity;

    public MenuProductRequest() {}

    private MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(productId, quantity);
    }

    public MenuProduct toMenuProduct(Product product) {
        return new MenuProduct(product.getId(), quantity);
    }

    public MenuProduct toMenuProduct(List<Product> products) {
        Product product = findProductByProductId(products);
        return new MenuProduct(product.getId(), quantity);
    }

    private Product findProductByProductId(List<Product> products) {
        return products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst().get();
    }
}
