package kitchenpos.product.domain;

import java.math.BigDecimal;

public class ProductFactory {

    public static Product create(long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }
}
