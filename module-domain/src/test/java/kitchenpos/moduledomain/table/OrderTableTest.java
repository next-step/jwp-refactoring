package kitchenpos.moduledomain.table;


import static java.util.Arrays.*;
import static kitchenpos.moduledomain.common.OrderFixture.주문;
import static kitchenpos.moduledomain.common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.moduledomain.common.exception.Message;
import kitchenpos.moduledomain.order.Order;
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
        assertThat(첫번째_주문테이블.getNumberOfGuests()).isEqualTo(new NumberOfGuests(3));
    }

    @Test
    void 주문테이블_생성시_빈테이블_여부값이_비어있으면_예외() {
        assertThatThrownBy(() -> {
            OrderTable.of(new NumberOfGuests(3), null);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_TABLE_IS_NOT_ORDER_TABLE_STATUS_NULL.getMessage());
    }


    @Test
    void 요리중이거나_식사중이_아니면_빈테이블로_변경_가능() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // when
        첫번째_주문테이블.changeOrderTableStatus(true);

        assertThat(첫번째_주문테이블.getOrderTableStatus()).isEqualTo(OrderTableStatus.EMPTY);
    }

    @Test
    void 요리중이거나_식사중이면_빈테이블로_변경시_예외() {
        // given
        Order 주문 = 주문();
        OrderTable 첫번째_주문테이블 = OrderTable.of(1L, new NumberOfGuests(3), OrderTableStatus.USE, asList(주문));

        // then
        assertThatThrownBy(() -> {
            첫번째_주문테이블.changeOrderTableStatus(true);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}