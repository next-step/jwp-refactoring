package kitchenpos.product.fixture;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 피자 = create(1L, Name.of("피자"), Price.of(BigDecimal.valueOf(22_000)));
    public static Product 치킨 = create(2L, Name.of("치킨"), Price.of(BigDecimal.valueOf(18_000)));
    public static Product 양념_치킨 = create(3L, Name.of("양념_치킨"), Price.of(BigDecimal.valueOf(18_000)));

    public static Product create(Long id, Name name, Price price) {
        return Product.of(id, name, price);
    }
}
