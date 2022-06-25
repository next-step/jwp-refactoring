package kitchenpos.domain;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductTest {

    public static Product 상품_생성(String name, int price) {
        return new Product(name, new BigDecimal(price));
    }
}
