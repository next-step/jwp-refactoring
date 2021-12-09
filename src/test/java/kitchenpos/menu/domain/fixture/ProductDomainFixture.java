package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.product.Product;

import java.math.BigDecimal;

public class ProductDomainFixture {
    public static Product 후라이드 = product("후라이드", BigDecimal.valueOf(15000));

    public static Product product(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
