package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class OrderTableTest {

    @DisplayName("단체 지정이 되어있지 않은 주문 테이블은 empty 를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(0, true);

        // when && then
        assertDoesNotThrow(() -> orderTable.changeEmpty(false));
    }

    @DisplayName("단체 지정이 되어있는 주문 테이블은 empty 를 변경할 수 없다.")
    @Test
    void changeEmptyException() {
        // given
        OrderTable orderTable = new OrderTable(
            1L, new TableGroup(1L, LocalDateTime.now()), 0, true);

        // when && then
        assertThatThrownBy(() -> orderTable.changeEmpty(false))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(ExceptionMessage.CANNOT_CHANGE_STATUS.getMessage());
    }

}
