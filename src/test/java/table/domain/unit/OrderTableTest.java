package table.domain.unit;

import static fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import common.*;
import table.domain.*;

@DisplayName("주문 테이블 관련(단위테스트)")
class OrderTableTest {
    @DisplayName("주문 테이블 생성하기")
    @Test
    void create() {
        assertThat(
            OrderTable.of(주문테이블_4명.getNumberOfGuests(), 주문테이블_4명.isEmpty())
        ).isInstanceOf(OrderTable.class);
    }

    @DisplayName("방문한 손님 수를 음수로 할 때, 주문 테이블 생성 실패함")
    @Test
    void exceptionTest1() {
        assertThatThrownBy(
            () -> OrderTable.of(-1, 주문테이블_4명.isEmpty())
        ).isInstanceOf(WrongValueException.class);
    }
}
