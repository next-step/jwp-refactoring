package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static kitchenpos.common.Messages.NUMBER_OF_GUESTS_CANNOT_ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

class NumberOfGuestsTest {

    @DisplayName("손님(Guests) 생성 테스트")
    @ParameterizedTest(name = "{displayName} 손님수: {0}")
    @ValueSource(ints = {0, 1})
    void numberOfGuests(int guests) {
        // when
        NumberOfGuests numberOfGuests = NumberOfGuests.of(guests);

        // then
        assertAll(
                () -> assertThat(numberOfGuests).isNotNull(),
                () -> assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(guests)
        );
    }

    @Test
    @DisplayName("손님 수 생성 실패 테스트")
    void numberOfGuestsCannotZero() {
        // given
        int guests = -1;

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> NumberOfGuests.of(guests))
                .withMessage(NUMBER_OF_GUESTS_CANNOT_ZERO)
        ;
    }
}
