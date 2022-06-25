package kitchenpos.domain;

import static kitchenpos.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @DisplayName("변경할 손님수가 0보다 작으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuest_fail_negative() {
        //given
        OrderTable orderTable = 주문테이블_데이터_생성(1L, null, 2, true);
        //when //then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTable.changeNumberOfGuest(-1));
    }

    @DisplayName("주문테이블이 비어있으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuest_fail_emptyOrderTable() {
        //given
        OrderTable orderTable = 주문테이블_데이터_생성(1L, null, 2, true);
        //when //then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderTable.changeNumberOfGuest(4));
    }
}