package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 후라이드치킨 = create("후라이드치킨", BigDecimal.valueOf(15_000));
    public static Product 강정치킨 = create("강정치킨", BigDecimal.valueOf(13_000));

    public static Product create(String name, BigDecimal price) {
        Product product = new Product(name, price);
        return product;
    }

}
