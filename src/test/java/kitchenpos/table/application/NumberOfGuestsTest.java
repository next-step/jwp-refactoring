package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.table.domain.NumberOfGuests;
import org.junit.jupiter.api.Test;

class NumberOfGuestsTest {
    @Test
    void 손님_수가_0보다_작으면_에러가_발생해야_한다() {
        // given
        final int given = -1;

        // when and then
        assertThatThrownBy(() -> new NumberOfGuests(given))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
