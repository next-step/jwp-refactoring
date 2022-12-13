package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {

    @DisplayName("0명 이상의 손님을 생성 할 수 있다.")
    @ParameterizedTest(name = "#{index} - {0}명의 손님을 생성할 수 있다.")
    @ValueSource(ints = {0, 6, 10})
    void testCreate(int number) {
        // given
        NumberOfGuests numberOfGuests = NumberOfGuests.of(number);
        // when && then
        assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(number);
    }

    @DisplayName("손님의 수는 0보다 작을 수 없다.")
    @ParameterizedTest(name = "#{index} - {0}명의 손님을 생성할 수 없다.")
    @ValueSource(ints = {-1, -2, -3})
    void NumberOfGuestsGreaterThanOrEqualToZero(int number) {
        // when && then
        assertThatThrownBy(() -> NumberOfGuests.of(number))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
