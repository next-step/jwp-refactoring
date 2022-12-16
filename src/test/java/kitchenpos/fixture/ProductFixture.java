package kitchenpos.fixture;

import kitchenpos.menu.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product create(String name, BigDecimal price) {
        return new Product(name, price);
    }

}
