package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GuestNumberTest {

    @Test
    @DisplayName("고객수 객체가 같은지 검증")
    void verifyEqualsGuestNumber() {
        assertThat(GuestNumber.of(5)).isEqualTo(GuestNumber.of(5));
    }

    @Test
    @DisplayName("음수 값이 들어오면 예외 발생")
    void invalidGuestNumber() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> GuestNumber.of(-5));
    }
}
