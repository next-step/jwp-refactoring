package kitchenpos.product.domain;

import java.math.BigDecimal;

public class ProductTestFixture {

    public static Product create(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product create(String name, BigDecimal price) {
        return new Product(null, name, price);
    }
}
