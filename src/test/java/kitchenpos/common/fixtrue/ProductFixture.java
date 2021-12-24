package kitchenpos.common.fixtrue;

import kitchenpos.product.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    private ProductFixture() {

    }

    public static Product of(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }
}
