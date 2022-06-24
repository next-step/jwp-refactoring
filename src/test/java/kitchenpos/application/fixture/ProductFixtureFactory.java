package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixtureFactory {

    private ProductFixtureFactory() {
    }

    public static Product create(final Long id, final String name, final BigDecimal price) {
        return  new Product(id, name, price);
    }

    public static Product create(final String name, final BigDecimal price) {
        return  new Product(name, price);
    }
}
