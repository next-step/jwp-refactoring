package kitchenpos.table.domain;

import kitchenpos.table.message.NumberOfGuestsMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {

    @Test
    @DisplayName("손님수 생성시 성공한다")
    void createNumberOfGuestsTest() {
        // when
        NumberOfGuests numberOfGuests = NumberOfGuests.of(3);

        // then
        assertThat(numberOfGuests).isEqualTo(NumberOfGuests.of(3));
    }

    @Test
    @DisplayName("손님수 생성시 0미만의 명수가 주어진 경우 예외처리되어 생성에 실패한다")
    void createNumberOfGuestsThrownByLessThanZeroTest() {
        // when
        assertThatThrownBy(() -> NumberOfGuests.of(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NumberOfGuestsMessage.CREATE_ERROR_GUESTS_MUST_BE_MORE_THAN_ZERO.message());
    }

    @Test
    @DisplayName("손님수 생성시 명수가 누락된 경우 예외처리되어 생성에 실패한다")
    void createNumberOfGuestsThrownByNullTest() {
        // when
        assertThatThrownBy(() -> NumberOfGuests.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NumberOfGuestsMessage.CREATE_ERROR_GUESTS_MUST_BE_NOT_NULL.message());
    }
}
