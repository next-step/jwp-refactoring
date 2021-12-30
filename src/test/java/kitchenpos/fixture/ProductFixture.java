package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

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
