package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {
    public static final Product product1 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(16000L));;
    public static final Product product2 = Product.of(2L, "양념치킨", BigDecimal.valueOf(16000L));
    public static final Product product3 = Product.of(3L, "'반반치킨'", BigDecimal.valueOf(16000L));
}
