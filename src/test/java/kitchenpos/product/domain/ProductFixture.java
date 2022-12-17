package kitchenpos.product.domain;

import java.math.BigDecimal;

import kitchenpos.product.dto.ProductRequest;

public class ProductFixture {
    public ProductFixture() {
    }

    public static ProductRequest productRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static Product savedProduct(long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product savedProduct(long id, BigDecimal price) {
        return new Product(id, null, price);
    }
}
