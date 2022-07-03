package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuantityTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Quantity quantity = Quantity.from(10);
        assertThat(quantity.value()).isEqualTo(10);
    }

    @DisplayName("최소값 아래 경우 테스트")
    @Test
    void ofWithUnderMin() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Quantity.from(-100))
                .withMessage("수량은 " + Quantity.MIN + "미만일 수 없습니다.");
    }
}
