package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {
    @DisplayName("손님 수는 음수일 수 없다.")
    @Test
    void createWithNegative() {
        assertThatThrownBy(() -> new NumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 음수 일 수 없습니다.");
    }
}
