package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 Domain 단위 테스트")
class OrderTableTest {

    @DisplayName("빈 테이블 여부 변경이 가능한지 확인한다.")
    @Test
    void checkPossibleChangeEmpty() {

        //given
        OrderTable orderTable = new OrderTable(1L, 3, false);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(orderTable::checkPossibleChangeEmpty);

    }
}
