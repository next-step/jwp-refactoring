package kitchenpos;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class TestProductFactory {
    public static Product create(Long id, String name, BigDecimal price) {
        return new Product(id, new Name(name), new Price(price));
    }
}
