package kitchenpos;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

public class TestProductFactory {
    public static Product create(Long id, String name, int price) {
        return new Product(id, new Name(name), new Price(price));
    }
}
