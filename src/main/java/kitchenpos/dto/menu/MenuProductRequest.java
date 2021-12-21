package kitchenpos.dto.menu;

import java.security.InvalidParameterException;
import java.util.List;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

public class MenuProductRequest {

    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

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

    public MenuProduct toMenuProduct(List<Product> products) {
        Product product = products.stream()
            .filter(it -> it.isSame(productId))
            .findFirst()
            .orElseThrow(() -> new InvalidParameterException("주문 항목이 존재하지 않습니다."));
        return new MenuProduct(product, quantity);
    }
}
