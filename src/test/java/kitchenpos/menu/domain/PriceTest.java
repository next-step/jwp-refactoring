package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("금액 객체가 같은지 검증")
    void verifyEqualsPrice() {
        assertThat(Price.of(100L)).isEqualTo(Price.of(100L));
    }

    @Test
    @DisplayName("금액 객체에 음수가 들어오면 예외 발생")
    void invalidInputPrice() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(-100L));
    }
}
