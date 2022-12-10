package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductFixture {
    public ProductFixture() {
    }

    public static Product productParam(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public static Product savedProduct(long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product savedProduct(long id, BigDecimal price) {
        return new Product(id, null, price);
    }
}
