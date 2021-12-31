package kitchenpos.core;

import kitchenpos.core.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTableTest {

    @DisplayName("방문한 손님 수는 1명 이상이어야 한다.")
    @Test
    void numberOfGuests() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> OrderTable.of(-1, false);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블이 아닐 경우 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsSuccess() {
        // given
        final OrderTable orderTable = OrderTable.of(5, false);
        // when
        orderTable.changeNumberOfGuests(13);
        // then
        assertTrue(orderTable.matchNumberOfGuests(13));
    }

    @DisplayName("빈 테이블이 아닐 경우 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsFail() {
        // given
        final OrderTable orderTable = OrderTable.of(5, true);
        // when
        ThrowableAssert.ThrowingCallable createCall = () -> orderTable.changeNumberOfGuests(13);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
