package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("손님수")
class NumberOfGuestsTest {

    @DisplayName("손님수를 생성한다.")
    @Test
    void create() {
        Assertions.assertThatNoException().isThrownBy(() -> new NumberOfGuests(1));
    }

    @DisplayName("손님수가 0명보다 적을 수 없다.")
    @Test
    void create_fail_minimum() {
        Assertions.assertThatThrownBy(() -> new NumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
