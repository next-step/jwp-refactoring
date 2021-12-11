package kitchenpos.application.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductFixtureFactory {

    private ProductFixtureFactory() {}

    public static Product create(long id, String name, long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
