package kitchenpos.utils.fixture;

import kitchenpos.product.domain.Product;

public class ProductFixtureFactory {
    public static Product createProduct(String name, int price) {
        return Product.of(name, price);
    }
}
