package kitchenpos.utils;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class DomainFixtureFactory {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        return Product.from(id, name, price);
    }

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
