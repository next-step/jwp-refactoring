package kitchenpos.fixture;

import kitchenpos.domain.Product;

import static java.math.BigDecimal.valueOf;

public class ProductFixture {
    public static final Product 상품_후라이드_치킨 = new Product(1L, "후라이드 치킨", valueOf(18_000));
    public static final Product 상품_양념_치킨 = new Product(2L, "양념 치킨", valueOf(18_000));
    public static final Product 상품_무료_콜라_서비스 = new Product(3L, "무료_콜라_서비스", valueOf(0));
    public static final Product 상품_감자튀김 = new Product(4L, "감자튀김", valueOf(2_000));
    public static final Product 상품_치즈볼 = new Product(5L, "치즈볼", valueOf(4_000));
    public static final Product 상품_옛날통닭 = new Product(6L, "옛날통닭", valueOf(14_000));
}
