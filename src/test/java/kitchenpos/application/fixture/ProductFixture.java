package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product create(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }
}
