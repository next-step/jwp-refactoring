package kitchenpos.fixture;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class ProductFixture {
    public ProductFixture() {
    }

    public static ProductRequest productRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static Product savedProduct(long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static Product savedProduct(long id, BigDecimal price) {
        return Product.of(id, null, price);
    }
}
