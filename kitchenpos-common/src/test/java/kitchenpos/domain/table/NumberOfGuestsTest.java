package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {

    @DisplayName("인원이 0보다 작은 경우 예외 발생")
    @Test
    void 인원_수_생성_예외() {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> NumberOfGuests.of(-2)
            )
            .withMessage("손님의 수는 0보다 커야 합니다");
    }

    @DisplayName("인원은 0으로 생성이 가능하다")
    @Test
    void 인원_수_생성() {
        // when
        NumberOfGuests result = NumberOfGuests.of(0);

        // then
        assertThat(result).isEqualTo(NumberOfGuests.of(0));
    }

}