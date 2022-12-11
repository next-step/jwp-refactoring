package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product 후라이드치킨 = create(1L, "후라이드치킨", BigDecimal.valueOf(15_000));
    public static Product 강정치킨 = create(2L, "강정치킨", BigDecimal.valueOf(13_000));

    public static Product create(Long id, String name, BigDecimal price) {
        Product product = new Product(id, name, price);
        return product;
    }

}
