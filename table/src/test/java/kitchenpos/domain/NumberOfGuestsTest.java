package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.NumberOfGuests;

class NumberOfGuestsTest {
    @DisplayName("방문한 손님수 음수 불가")
    @Test
    void generate_negative() {
        assertThatThrownBy(() -> NumberOfGuests.from(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
