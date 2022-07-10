package kitchenpos.product;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class ProductTestFixture {
    public static Product 상품_생성(String name) {
        return new Product(name, null);
    }

    public static Product 상품_생성(String name, Long price) {
        return new Product(name, new BigDecimal(price));
    }

    public static Product 상품_생성(Long id, String name, Long price) {
        return new Product(id, name, new BigDecimal(price));
    }
}
