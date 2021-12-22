package kitchenpos.tobe.fixture;

import kitchenpos.tobe.product.domain.Name;
import kitchenpos.tobe.product.domain.Price;
import kitchenpos.tobe.product.domain.Product;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product of(final Long id, final Name name, final Price price) {
        return new Product(id, name, price);
    }

    public static Product of(final Name name, final Price price) {
        return of(null, name, price);
    }
}
