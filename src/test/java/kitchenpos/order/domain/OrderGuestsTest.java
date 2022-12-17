package kitchenpos.order.domain;

import kitchenpos.exception.OrderTableErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("주문 테이블 방문자 단위 테스트")
class OrderGuestsTest {

    @DisplayName("방문자 수가 동일하면 주문 방문자는 동일하다.")
    @Test
    void 방문자_수가_동일하면_주문_방문자는_동일하다() {
        assertEquals(
                new OrderGuests(5),
                new OrderGuests(5)
        );
    }

    @DisplayName("주문 테이블 방문자 수는 음수일 수 없다.")
    @Test
    void 주문_테이블_방문자_수는_음수일_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderGuests(-1))
                .withMessage(OrderTableErrorMessage.INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @DisplayName("빈 주문 테이블이면 방문자 수를 변경할 수 없다.")
    @Test
    void 빈_주문_테이블이면_방문자_수를_변경할_수_없다() {
        OrderGuests guests = new OrderGuests(5);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> guests.changeNumberOfGuests(5, true))
                .withMessage(OrderTableErrorMessage.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
    }
}
