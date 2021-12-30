package kitchenpos.moduledomain.table;

import static kitchenpos.moduledomain.common.OrderTableFixture.단체지정_두번째_주문테이블;
import static kitchenpos.moduledomain.common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static kitchenpos.moduledomain.common.OrderTableFixture.두번째_주문테이블;
import static kitchenpos.moduledomain.common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.moduledomain.common.exception.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    void 빈_테이블이_아니면_생성불가() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_주문테이블(), 두번째_주문테이블());

        assertThatThrownBy(() -> {
            OrderTables.of(orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_TABLE_IS_NOT_EMPTY_TABLE_OR_ALREADY_GROUP.getMessage());
    }

    @Test
    void 최소사이즈보다_작으면_생성불가() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_주문테이블());

        assertThatThrownBy(() -> {
            OrderTables.of(orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_TABLES_IS_SMALL_THAN_MIN_TABLE_SIZE.getMessage());
    }

    @Test
    void 주문테이블_생성() {
        // given
        List<OrderTable> orderTables = Arrays.asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블());

        // when
        OrderTables orderTable = OrderTables.of(orderTables);

        // then
        Assertions.assertThat(orderTable).isEqualTo(orderTable);
    }

}