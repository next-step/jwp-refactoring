package kitchenpos.fixture;

import kitchenpos.domain.Product;

import static java.math.BigDecimal.valueOf;

public class ProductFixture {
    public static final Product 상품_후라이드_치킨 = new Product(1L, "후라이드 치킨", valueOf(18_000));
    public static final Product 상품_양념_치킨 = new Product(2L, "양념 치킨", valueOf(18_000));
    public static final Product 상품_무료_콜라_서비스 = new Product(3L, "무료_콜라_서비스", valueOf(0));
}
