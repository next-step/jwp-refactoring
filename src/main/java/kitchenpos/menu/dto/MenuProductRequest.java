package kitchenpos.menu.dto;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    protected MenuProductRequest() {}

    private MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static MenuProductRequest from(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuProduct createMenuProduct(List<Product> products) {
        Product product = findProduct(products);
        return new MenuProduct(quantity, product);
    }

    private Product findProduct(List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .get();
    }
}
