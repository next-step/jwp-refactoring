package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

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
                .filter(product -> product.equalsId(productId))
                .findFirst().get();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
