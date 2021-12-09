package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.product.Product;

import java.math.BigDecimal;

public class ProductDomainFixture {
    public static Product product(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
