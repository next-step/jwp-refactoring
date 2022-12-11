package kitchenpos.product.domain;

import java.math.BigDecimal;

public class ProductFixture {
    public static Product 양념치킨 = new Product("양념치킨", BigDecimal.valueOf(18000));
    public static Product 후라이드치킨 = new Product("후라이드치킨", BigDecimal.valueOf(15000));
    public static Product 콜라 = new Product("콜라", BigDecimal.valueOf(1000));
}
