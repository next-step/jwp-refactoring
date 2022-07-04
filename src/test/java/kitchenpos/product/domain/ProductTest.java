package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품 객체는 0원 이상의 금액을 가진다")
    void constructorTest() {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Product("양념치킨", BigDecimal.valueOf(-1))
        );
    }
}
