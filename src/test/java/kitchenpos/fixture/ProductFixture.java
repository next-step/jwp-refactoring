package kitchenpos.fixture;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product create(String name, BigDecimal price) {
        return new Product(name, price);
    }

}
