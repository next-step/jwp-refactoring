package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

import java.util.List;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public MenuProduct toMenuProducts(final Menu menu, final List<Product> products) {
        return MenuProduct.builder()
                .menu(menu)
                .product(products.stream().filter(product -> product.getId().equals(productId)).findFirst().get())
                .quantity(quantity)
                .build();
    }
}
