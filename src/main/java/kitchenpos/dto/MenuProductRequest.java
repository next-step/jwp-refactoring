package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.util.List;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }


    public MenuProduct makeMenuProduct(List<Product> product) {
        Product targetProduct = findProduct(product);
        return MenuProduct.of(null, targetProduct, quantity);
    }

    private Product findProduct(List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
