package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("손님 수")
class NumberOfGuestsTest {
    @DisplayName("손님 수 객체를 생성할 수 있다.")
    @Test
    void 생성_성공() {
        assertThat(NumberOfGuests.from(10)).isNotNull();
    }

    @DisplayName("손님 수가 0보다 작으면 생성할 수 없다.")
    @Test
    void 손님_수가_0보다_작은_경우() {
        assertThatThrownBy(() -> NumberOfGuests.from(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}
