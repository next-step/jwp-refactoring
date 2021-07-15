package kitchenpos.table.domain;

import kitchenpos.exception.CannotUpdateException;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.Message.ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_CHANGED_WHEN_EMPTY;
import static kitchenpos.common.Message.ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_SMALLER_THAN_ZERO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableTest {

    @DisplayName("단체지정된 테이블의 비어있음 여부를 변경하는 경우, 예외발생한다")
    @Test
    void 단체지정된_테이블의_비어있음_여부_변경시_예외발생() {
        Long 단체지정_ID = 1L;
        OrderTable 단체지정_된_테이블 = new OrderTable(단체지정_ID, 3, false);

        assertThatThrownBy(() -> 단체지정_된_테이블.changeEmpty(true))
                .isInstanceOf(CannotUpdateException.class);
    }

    @DisplayName("테이블의 손님수를 0명 미만으로 입력하여 변경시, 예외 발생한다")
    @Test
    void 테이블의_손님수를_0명미만으로_입력하여_변경시_예외발생() {
        Long 단체지정_ID = 1L;
        OrderTable 테이블 = new OrderTable(단체지정_ID, 3, false);
        int 음수 = -10;

        assertThatThrownBy(() -> 테이블.updateNumberOfGuestsTo(음수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_SMALLER_THAN_ZERO.showText());
    }

    @DisplayName("비어있는 테이블의 손님수를 변경시, 예외 발생한다")
    @Test
    void 비어있는_테이블의_손님수_변경시_예외발생() {
        Long 단체지정_ID = 1L;
        OrderTable 비어있는_테이블 = new OrderTable(단체지정_ID, 3, true);

        assertThatThrownBy(() -> 비어있는_테이블.updateNumberOfGuestsTo(5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_CHANGED_WHEN_EMPTY.showText());
    }
}
