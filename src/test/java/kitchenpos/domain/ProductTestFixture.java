package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTestFixture {
    public static Product product(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
