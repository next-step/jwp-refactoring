package kitchenpos.factory;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixtureFactory {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, price);
    }
}
