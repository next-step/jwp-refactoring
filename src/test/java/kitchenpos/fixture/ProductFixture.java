package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static final Product 페퍼로니 = new Product("페퍼로니", new BigDecimal(12_000));
    public static final Product 후라이드 = new Product("후라이드", new BigDecimal(16_000));
}
