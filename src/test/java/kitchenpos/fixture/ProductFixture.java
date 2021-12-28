package kitchenpos.fixture;

import java.math.*;

import kitchenpos.menu.domain.*;

public class ProductFixture {
    public static final Product 상품_후라이드치킨 = Product.of("후라이드치킨", BigDecimal.valueOf(16000L));;
    public static final Product 상품_양념치킨 = Product.of("양념치킨", BigDecimal.valueOf(16000L));
    public static final Product 상품_반반치킨 = Product.of("반반치킨", BigDecimal.valueOf(16000L));
}
