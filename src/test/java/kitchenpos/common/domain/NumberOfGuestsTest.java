package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberOfGuestsTest {

    @DisplayName("손님 수가 0명보다 작으면 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -10, -100 })
    void validateNumberOfGuestsLessThanZero(int numberOfGuests) {
        // when & then
        assertThatThrownBy(() -> NumberOfGuests.from(numberOfGuests))
            .isInstanceOf(IllegalArgumentException.class);
    }
}