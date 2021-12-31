package kitchenpos.ordertable.domain;

import kitchenpos.ordertable.infra.JpaOrderTableRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderTableTest {

    @Autowired
    private JpaOrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블은 아이디, 방문한 손님 수, 빈 테이블 유무로 구성되어 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = OrderTable.of(13, false);
        // when
        final OrderTable actual = orderTableRepository.save(orderTable);
        // then
        assertAll(
                () -> assertTrue(actual.matchNumberOfGuests(13)),
                () -> assertFalse(actual.isEmpty())
        );
    }

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
