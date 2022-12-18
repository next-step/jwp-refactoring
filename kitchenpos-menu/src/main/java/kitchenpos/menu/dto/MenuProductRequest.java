package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.util.List;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest() {}

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct(Product product) {
        return MenuProduct.of(product, quantity);
    }

    private Product findProductByProductId(List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst().get();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
