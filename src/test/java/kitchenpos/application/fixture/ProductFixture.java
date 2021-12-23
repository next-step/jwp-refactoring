package kitchenpos.application.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product 후리이드치킨() {
        return Product.of("후리이드치킨", 15000);
    }

}
