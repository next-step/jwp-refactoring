package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductFixtureFactory {
    private ProductFixtureFactory() {
    }

    public static Product createProduct(String name, int price) {
        return new Product(name, new BigDecimal(price));
    }

}
