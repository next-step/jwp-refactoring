package kitchenpos.domain;

import kitchenpos.exception.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {
    @DisplayName("손님은 0명 이하로는 생성 할 수 없다.")
    @Test
    void validate() {
        assertThatThrownBy(
                () -> NumberOfGuests.of(0)
        ).isInstanceOf(InvalidNumberOfGuestsException.class);
    }
}
