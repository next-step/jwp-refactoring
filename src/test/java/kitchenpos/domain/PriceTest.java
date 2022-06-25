package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Price price = Price.from(BigDecimal.TEN);
        assertThat(price.value()).isEqualTo(BigDecimal.TEN);
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.from(null))
                .withMessage("금액을 지정해야 합니다.");
    }

    @DisplayName("최소값 아래 경우 테스트")
    @Test
    void ofWithUnderMin() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.from(BigDecimal.valueOf(-100)))
                .withMessage("금액은 " + Price.MIN + "원 이하가 될 수 없습니다.");
    }
}
