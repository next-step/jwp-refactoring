package kitchenpos.table.domain;

import static common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTableTest {


    @Test
    void 주문_된_테이블로_변경한다() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // when
        첫번째_주문테이블.changeOrderTableStatus(false);

        // then
        Assertions.assertFalse(첫번째_주문테이블.isEmpty());
    }

    @Test
    void 빈_테이블로_변경한다() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // when
        첫번째_주문테이블.changeOrderTableStatus(true);

        // then
        Assertions.assertTrue(첫번째_주문테이블.isEmpty());
    }

    @Test
    void 손님_수_변경_한다() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // when
        첫번째_주문테이블.changeNumberOfGuest(3);

        // then
        assertThat(첫번째_주문테이블.getNumberOfGuests()).isEqualTo(3);
    }
}