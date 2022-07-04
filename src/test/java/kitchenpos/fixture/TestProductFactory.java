package kitchenpos.fixture;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;

public class TestProductFactory {
    public static Product create(Long id, String name, int price) {
        return new Product(id, new Name(name), new Price(price));
    }
}
