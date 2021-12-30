package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;

import java.math.BigDecimal;

public class ProductTest {
    public static final Product 후라이드_상품 = new Product(1L, "후라이드", new Price(BigDecimal.valueOf(16000)));
    public static final Product 양념치킨_상품 = new Product(2L, "양념치킨", new Price(BigDecimal.valueOf(16000)));
    public static final Product 반반치킨_상품 = new Product(3L, "반반치킨 상품", new Price(BigDecimal.valueOf(16000)));
    public static final Product 통구이_상품 = new Product(4L, "통구이 상품", new Price(BigDecimal.valueOf(16000)));
    public static final Product 간장치킨_상품 = new Product(5L, "간장치킨 상품", new Price(BigDecimal.valueOf(17000)));
    public static final Product 순살치킨_상품 = new Product(6L, "순살치킨 상품", new Price(BigDecimal.valueOf(17000)));
}
