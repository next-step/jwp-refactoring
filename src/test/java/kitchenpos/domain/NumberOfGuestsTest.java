package kitchenpos.domain;

import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidNumberOfGuestsSize;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("방문자 수 테스트")
class NumberOfGuestsTest {

    @DisplayName("방문자 수가 다른 객체는 동등하지 않다.")
    @Test
    void equalsTest() {
        NumberOfGuests numberOfGuests1 = NumberOfGuests.from(10);
        NumberOfGuests numberOfGuests2 = NumberOfGuests.from(5);

        Assertions.assertThat(numberOfGuests1).isNotEqualTo(numberOfGuests2);
    }

    @DisplayName("방문자 수가 같은 객체는 동등하다.")
    @Test
    void equalsTest2() {
        NumberOfGuests numberOfGuests1 = NumberOfGuests.from(10);
        NumberOfGuests numberOfGuests2 = NumberOfGuests.from(10);

        Assertions.assertThat(numberOfGuests1).isEqualTo(numberOfGuests2);
    }

    @DisplayName("방문자 수가 0 미만이면 예외가 발생한다.")
    @Test
    void createException() {
        Assertions.assertThatThrownBy(() -> NumberOfGuests.from(-1))
                .isInstanceOf(InvalidNumberOfGuestsSize.class)
                .hasMessageStartingWith(ExceptionMessage.INVALID_NUMBER_OF_GUESTS_SIZE);
    }

    @DisplayName("방문자 수가 변경된다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"5:5", "10:10"}, delimiter = ':')
    void create(int input, int expected) {
        NumberOfGuests numberOfGuests = NumberOfGuests.from(input);

        Assertions.assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(expected);
    }
}
