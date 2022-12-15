package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    private MenuProductRequest() {}

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public MenuProduct toMenuProducts(Menu menu, List<Product> products) {
        Product target = findProductByProductId(products);
        return MenuProduct.of(menu, target, quantity);
    }

    private Product findProductByProductId(List<Product> products) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
