package kitchenpos.tobe.fixture;

import java.math.BigDecimal;
import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.product.domain.Product;
import kitchenpos.tobe.product.dto.ProductRequest;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product of(final Long id, final Name name, final Price price) {
        return new Product(id, name, price);
    }

    public static Product of(final Name name, final Price price) {
        return of(null, name, price);
    }

    public static Product of(final Long id, final String name, final BigDecimal price) {
        return of(id, new Name(name), new Price(price));
    }

    public static ProductRequest ofRequest(final String name, final BigDecimal price) {
        return new ProductRequest(name, price);
    }
}
