package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductFixture {

    public static Product 상품_요청_데이터_생성(String name, BigDecimal price) {
        return new Product(null, name, price);
    }

    public static Product 상품_데이터_생성(Long id, String name, BigDecimal price) {
        return new Product(id, name, price);
    }

    public static void 상품_데이터_확인(Product product, Long id, String name, BigDecimal price) {
        assertAll(
                () -> assertEquals(id, product.getId()),
                () -> assertEquals(name, product.getName()),
                () -> assertEquals(price, product.getPrice())
        );
    }
}
