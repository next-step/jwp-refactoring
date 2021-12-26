package kitchenpos.tobe.orders.domain.ordertable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class NumberOfGuestsTest {

    @DisplayName("방문한 손님 수를 생성할 수 있다.")
    @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
    @ValueSource(ints = {0, Integer.MAX_VALUE})
    void create(final int numberOfGuests) {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> new NumberOfGuests(numberOfGuests);

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("방문한 손님 수는 0보다 커야 한다.")
    @Test
    void createFailNegativeNumberOfGuests() {
        // given
        final int numberOfGuests = Integer.MIN_VALUE;

        // when
        final ThrowableAssert.ThrowingCallable request = () -> new NumberOfGuests(numberOfGuests);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수 간 동등성을 비교할 수 있다.")
    @Test
    void equals() {
        // given
        final int numberOfGuests = 4;

        // when
        final NumberOfGuests numberOfGuests1 = new NumberOfGuests(numberOfGuests);
        final NumberOfGuests numberOfGuests2 = new NumberOfGuests(numberOfGuests);

        // then
        assertThat(numberOfGuests1).isEqualTo(numberOfGuests2);
    }
}
