package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        NumberOfGuests numberOfGuests = NumberOfGuests.from(10);
        assertThat(numberOfGuests.value()).isEqualTo(10);
    }

    @DisplayName("최소값 아래 경우 테스트")
    @Test
    void ofWithUnderMin() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> NumberOfGuests.from(-100))
                .withMessage("손님수가 " + NumberOfGuests.MIN + "미만일 수 없습니다.");
    }
}
