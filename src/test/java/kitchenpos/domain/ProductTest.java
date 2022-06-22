package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTest {

    public static Product 상품_생성(String name, int price) {
        Product product = new Product();

        product.setName(name);
        product.setPrice(new BigDecimal(price));

        return product;
    }
}
