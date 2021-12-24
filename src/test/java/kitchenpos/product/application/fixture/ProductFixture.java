package kitchenpos.product.application.fixture;

import kitchenpos.product.domain.Product;

public class ProductFixture {

    private ProductFixture() {
    }

    public static Product 후리이드치킨() {
        return Product.of("후리이드치킨", 15000);
    }

}
