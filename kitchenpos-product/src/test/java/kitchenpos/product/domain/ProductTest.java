package kitchenpos.product.domain;

import static kitchenpos.product.ProductTestFixture.상품_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("상품 가격이 null 이거나 음수일 경우 - 오류")
    void invalidPrice() {
        // when then
        assertAll(
            () -> assertThatThrownBy(() -> 상품_생성("떡볶이", -3_000L))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> 상품_생성("떡볶이"))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

//    public static Product 상품_생성(String name) {
//        return new Product(name, null);
//    }
//
//    public static Product 상품_생성(String name, Long price) {
//        return new Product(name, new BigDecimal(price));
//    }
//
//    public static Product 상품_생성(Long id, String name, Long price) {
//        return new Product(id, name, new BigDecimal(price));
//    }
}
