package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTest {

    public static Product 상품_생성(Long id, String name, BigDecimal price) {
        return new Product.Builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }
}