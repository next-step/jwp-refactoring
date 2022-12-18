package kitchenpos.product.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product productA() {
        return new Product(new Name("A"), new Price(BigDecimal.ONE));
    }
}
