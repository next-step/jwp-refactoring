package kitchenpos.product.fixture;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static final Product 강정치킨 = create(1L, "강정치킨", BigDecimal.valueOf(17_000));
    public static final Product 페퍼로니피자 = create(2L, "페퍼로니피자", BigDecimal.valueOf(20_000));

    private ProductFixture() {
        throw new UnsupportedOperationException();
    }

    public static Product create(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }
}
