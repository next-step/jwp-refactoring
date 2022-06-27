package kitchenpos.factory;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixtureFactory {
    public static Product createProduct(Long id, String name, int price) {
        return new Product(id, name, price);
    }

    public static Product createProduct(String name, int price) {
        return new Product(name, price);
    }
}
