package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTestFixture {

    public static Product generateProduct(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static Product generateProduct(String name, BigDecimal price) {
        return Product.of(null, name, price);
    }
}
