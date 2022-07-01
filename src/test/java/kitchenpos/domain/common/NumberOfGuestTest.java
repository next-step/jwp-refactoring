package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.domain.NumberOfGuests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NumberOfGuestTest {

    @Test
    @DisplayName("손님수는 0명 이상이어야 한다.")
    void validationTest() {
        assertThatThrownBy(
                () -> new NumberOfGuests(-1)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
