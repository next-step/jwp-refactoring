package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.util.List;

import static kitchenpos.constants.ErrorCodeType.NOT_FOUND_PRODUCT;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct makeMenuProduct(List<Product> product) {
        Product targetProduct = findProduct(product);
        return new MenuProduct(targetProduct, quantity);
    }

    private Product findProduct(List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MenuProductRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}


