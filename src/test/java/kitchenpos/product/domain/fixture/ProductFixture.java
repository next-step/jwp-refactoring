package kitchenpos.product.domain.fixture;

import kitchenpos.product.domain.Product;

import static kitchenpos.common.fixture.NameFixture.nameProductA;
import static kitchenpos.common.fixture.PriceFixture.priceProductA;

public class ProductFixture {

    public static Product createProductA() {
        return new Product(nameProductA(), priceProductA());
    }

    public static Product productA() {
        return new Product(1L, nameProductA(), priceProductA());
    }
}
