package kitchenpos.__fixture__;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductTestFixture {
    public static Product 상품_생성(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }
}
