package kitchenpos.product.fixture;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.springframework.test.util.ReflectionTestUtils;

public class ProductFixtureFactory {
    private ProductFixtureFactory() {
    }

    public static Product createProduct(String name, int price) {
        return new Product(name, new BigDecimal(price));
    }

    public static Product createProduct(Long id, String name, int price) {
        Product product = new Product(name, new BigDecimal(price));
        ReflectionTestUtils.setField(product, "id", id);
        return product;
    }

}
