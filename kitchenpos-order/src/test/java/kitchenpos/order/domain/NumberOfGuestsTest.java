package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {
    @DisplayName("손님 수는 0명 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 100})
    void numberOfGuest(int value) {
        // when
        NumberOfGuests numberOfGuests = new NumberOfGuests(value);

        // then
        assertThat(numberOfGuests.value()).isEqualTo(value);
    }

    @DisplayName("손님 수가 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, -4, -100})
    void numberOfGuestException(int value) {
        assertThatThrownBy(() -> new NumberOfGuests(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.NUMBER_OF_GUESTS_MINIMUM.getMessage());
    }
}
