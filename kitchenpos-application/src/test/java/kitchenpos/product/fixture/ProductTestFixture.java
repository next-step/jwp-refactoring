package kitchenpos.product.fixture;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductTestFixture {

	public static final Product 후라이드 = Product.of(1L, "후라이드 치킨", BigDecimal.valueOf(15_000));
}
