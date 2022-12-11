package kitchenpos.domain;

import java.math.BigDecimal;

public class ProductTestFixture {

    public static Product createProduct(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static Product createProduct(String name, BigDecimal price) {
        return Product.of(null, name, price);
    }

    public static Product 단무지() {
        return createProduct(4L, "단무지", BigDecimal.valueOf(0L));
    }

    public static Product 짬뽕() {
        return createProduct(3L, "짬뽕", BigDecimal.valueOf(9000L));
    }

    public static Product 탕수육() {
        return createProduct(2L, "탕수육", BigDecimal.valueOf(12000L));
    }

    public static Product 짜장면() {
        return createProduct(1L, "짜장면", BigDecimal.valueOf(8000L));
    }
}
