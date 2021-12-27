package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class OrderTableTest {

    @DisplayName("방문한 손님수는 0명 이상으로 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void changeNumberOfGuests(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.of(5, false);

        // when && then
        assertDoesNotThrow(() -> orderTable.changeNumberOfGuests(numberOfGuests));
    }

    @DisplayName("방문한 손님수는 0명 미만으로 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsLessThanZero() {
        // given
        OrderTable orderTable = OrderTable.of(5, false);

        // when && then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage());
    }

    @DisplayName("빈 테이블의 방문한 손님수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsEmptyTable() {
        // given
        OrderTable orderTable = OrderTable.of(5, true);

        // when && then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.CANNOT_CHANGE_STATUS.getMessage());
    }
}
