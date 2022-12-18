package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.util.List;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest() {}

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProducts(Menu menu, List<Product> products) {
        Product target = findProductByProductId(products);
        return new MenuProduct(menu, target, quantity);
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
