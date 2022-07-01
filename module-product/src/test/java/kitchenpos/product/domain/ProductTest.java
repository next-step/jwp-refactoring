package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Product product = Product.from(1L, "양념", BigDecimal.TEN);
        assertThat(product).isEqualTo(product);
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Product.from(1L, "양념", null))
                .withMessage("금액을 지정해야 합니다.");
    }

    @DisplayName("최소값 아래 경우 테스트")
    @Test
    void ofWithUnderMin() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Product.from(1L, "양념", BigDecimal.valueOf(-100)))
                .withMessage("금액은 " + Price.MIN + "원 미만이 될 수 없습니다.");
    }
}
