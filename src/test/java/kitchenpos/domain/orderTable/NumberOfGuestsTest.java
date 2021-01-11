package kitchenpos.domain.orderTable;

import kitchenpos.domain.orderTable.exceptions.InvalidNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NumberOfGuestsTest {
    @DisplayName("0명 미만의 손님수로 오브젝트를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -2 })
    void createFailTest(int invalidValue) {
        // when, then
        assertThatThrownBy(() -> new NumberOfGuests(invalidValue))
                .isInstanceOf(InvalidNumberOfGuestsException.class)
                .hasMessage("방문한 손님수는 0명 미만일 수 없습니다.");
    }
}