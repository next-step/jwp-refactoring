package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;

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
