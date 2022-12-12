package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 방문자 정보를 관리하는 클래스 테스트")
class TableGuestsTest {
    @ParameterizedTest
    @ValueSource(ints = { -1, -2, -5, -10 })
    void 주문_테이블_방문자_수는_음수일_수_없음(int numberOfGuests) {
        assertThatThrownBy(() -> {
            new TableGuests(numberOfGuests);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @Test
    void 빈_주문_테이블이면_방문자_수를_변경할_수_없음() {
        TableGuests guests = new TableGuests(5);

        assertThatThrownBy(() -> {
            guests.changeNumberOfGuests(5, true);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
    }
}