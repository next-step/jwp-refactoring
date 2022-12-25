package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.error.ErrorEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class NumberOfGuestsTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 20})
    void 손님을_객체를_생성한다(int value) {
        NumberOfGuests numberOfGuests = new NumberOfGuests(value);
        assertThat(numberOfGuests.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    void 손님은_0명_이하일_수_앖다(int value) {
        assertThatThrownBy(() -> new NumberOfGuests(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.GUESTS_UNDER_ZERO.message());
    }
}
