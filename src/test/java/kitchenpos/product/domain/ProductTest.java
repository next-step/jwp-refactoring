package kitchenpos.product.domain;

import java.math.BigDecimal;

public class ProductTest {

    public static Product 상품_생성(String name, int price) {
        return new Product(name, new BigDecimal(price));
    }

    public static Product 상품_생성(Long id, String name, int price) {
        return new Product(id, name, new BigDecimal(price));
    }
}
