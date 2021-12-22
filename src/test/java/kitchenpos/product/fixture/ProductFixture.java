package kitchenpos.product.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product create(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }
}
