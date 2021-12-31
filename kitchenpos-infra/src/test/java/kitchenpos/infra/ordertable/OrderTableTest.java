package kitchenpos.infra.ordertable;

import kitchenpos.core.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

}
