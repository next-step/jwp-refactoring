package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("방문한 손님 수 테스트")
class NumberOfGuestsTest {

    @DisplayName("손님수가 최소 인원보다 작으면 예외가 발생한다.")
    @Test
    void createLessThanMinimumNumberOfGuests() {
        assertThatThrownBy(() -> NumberOfGuests.of(NumberOfGuests.MINIMUM_GUEST_NUMBER - 1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}