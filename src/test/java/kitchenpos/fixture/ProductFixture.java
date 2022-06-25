package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 피자 = create(1L, "피자", BigDecimal.valueOf(22_000));
    public static Product 치킨 = create(2L, "치킨", BigDecimal.valueOf(18_000));
    public static Product 양념_치킨 = create(3L, "양념_치킨", BigDecimal.valueOf(18_000));

    public static Product create(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);

        return product;
    }
}
