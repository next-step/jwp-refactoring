package kitchenpos.product.domain.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.ProductPrice;

public class ProductFixtureFactory {
    public static ProductPrice createProductPrice(BigDecimal price) {
        return new ProductPrice(price);
    }
}
