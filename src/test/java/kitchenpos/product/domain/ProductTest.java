package kitchenpos.product.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("상품 도메인 테스트")
class ProductTest {

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // when
        Product product = new Product("후라이드치킨", new BigDecimal(16_000));

        // then
        assertThat(product).isEqualTo(new Product("후라이드치킨", new BigDecimal(16_000)));
    }

    @Test
    @DisplayName("0보다 작은 가격으로 상품을 생성하면 예외를 발생한다.")
    void createThrowException() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Product("후라이드치킨", new BigDecimal(-1)));
    }
}
