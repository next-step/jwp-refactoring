package kitchenpos.product.fixture;

import kitchenpos.common.domain.fixture.NameFixture;
import kitchenpos.common.domain.fixture.PriceFixture;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {
    private ProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static Product create(Long id, String name, BigDecimal price) {
        return Product.of(id, NameFixture.of(name), PriceFixture.of(price));
    }
}
