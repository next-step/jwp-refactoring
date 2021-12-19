package kitchenpos.common.fixture;

import java.math.BigDecimal;

import kitchenpos.common.domain.product.Product;

public class ProductFixtureFactory {

    private ProductFixtureFactory() {}

    public static Product create(long id, String name, long price) {
        return Product.of(id, name, BigDecimal.valueOf(price));
    }
}
