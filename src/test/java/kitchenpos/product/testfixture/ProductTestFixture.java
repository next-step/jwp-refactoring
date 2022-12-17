package kitchenpos.product.testfixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductTestFixture {

    public static Product create(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product create(String name, BigDecimal price) {
        return new Product(null, name, price);
    }
}
