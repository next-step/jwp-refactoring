package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtureFactory {

    private ProductFixtureFactory() {}

    public static Product create(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }
}
