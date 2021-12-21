package kitchenpos.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NumberOfGuestsTest {
    @DisplayName("0명 이상의 손님 수를 생성한다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void testCreate(int number) {
        // when
        NumberOfGuests numberOfGuests = NumberOfGuests.of(number);

        // then
        assertThat(numberOfGuests.value()).isEqualTo(number);
    }

    @DisplayName("손님 수는 0명 이상이어야 한다")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    void NumberOfGuestsGreaterThanOrEqualToZero(int number) {
        // when
        ThrowableAssert.ThrowingCallable callable = () -> NumberOfGuests.of(number);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
