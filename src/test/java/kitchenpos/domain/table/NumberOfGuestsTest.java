package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.exception.NegativeNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberOfGuestsTest {

    @DisplayName("NumberOfGuests를 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 100, 1000})
    void create01(int numberOfGuests) {
        // when & then
        assertThatNoException().isThrownBy(() -> NumberOfGuests.from(numberOfGuests));
    }

    @DisplayName("NumberOfGuests를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void create02(int numberOfGuests) {
        // when & then
        assertThatExceptionOfType(NegativeNumberOfGuestsException.class)
                .isThrownBy(() -> NumberOfGuests.from(numberOfGuests))
                .withMessageContaining(String.format(NumberOfGuests.INVALID_NUMBER_OF_GUESTS, numberOfGuests));
    }
}