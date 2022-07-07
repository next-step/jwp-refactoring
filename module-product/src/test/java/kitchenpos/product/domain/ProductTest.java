package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품 금액은 0원 이상이다")
    void constructorTest() {
        // given
        BigDecimal price = BigDecimal.ONE;

        // when
        Product actual = new Product("양념치킨", price);

        // then
        assertThat(actual.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("상품 객체는 0원 이상의 금액을 가진다")
    void constructorTest_error() {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Product("양념치킨", BigDecimal.valueOf(-1))
        ).withMessageContaining("유효하지 않은 금액입니다.");
    }
}
