package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidNumberOfGuestsException;
import kitchenpos.table.domain.NumberOfGuests;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {
    @Test
    void 음수_손님_예외() {
        assertThatThrownBy(
                () -> assertThat(NumberOfGuests.from(-10))
        ).isInstanceOf(InvalidNumberOfGuestsException.class);
    }
}
