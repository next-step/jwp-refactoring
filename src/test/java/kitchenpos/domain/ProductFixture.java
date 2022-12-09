package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductFixture {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return new Product(null, name, price);
    }
}
