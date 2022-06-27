package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NumberOfGuestsTest {

    @ParameterizedTest
    @DisplayName("음수의 손님수는 생성할수 없다")
    @ValueSource(ints = {-1, -2, -3, -4, -5})
    public void cantCreateMinusGuest(int guestCount) {
        assertThatThrownBy(() -> new NumberOfGuests(guestCount)).isInstanceOf(
            IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("음수의 손님수는 설정할수 없다")
    @ValueSource(ints = {-1, -2, -3, -4, -5})
    public void cantSetMinusGuest(int guestCount) {
        NumberOfGuests numberOfGuests = new NumberOfGuests(3);
        assertThatThrownBy(() -> numberOfGuests.changeNumberOfGuests(guestCount)).isInstanceOf(
            IllegalArgumentException.class);
    }

}