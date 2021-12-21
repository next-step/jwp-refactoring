package kitchenpos.ui.fixtrue;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private ProductFixture() {

    }

    public static Product of(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
