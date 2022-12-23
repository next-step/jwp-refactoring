package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.util.List;
import java.util.Optional;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public MenuProductRequest() {}

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProducts(Menu menu, List<Product> products) {
        Optional<Product> target = findProductByProductId(products);
        if (!target.isPresent()) {
            throw new IllegalArgumentException();
        }

        return new MenuProduct(menu, target.get(), quantity);
    }

    private Optional<Product> findProductByProductId(List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst();
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
