package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    void 두개_미만의_테이블은_단체지정할_수_없다() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(
                new OrderTable(1, true)
        );

        // when & then
        assertThatThrownBy(() ->
                new TableGroup(orderTables)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2개 이상의 테이블을 단체 지정할 수 있습니다.");
    }

    @Test
    void 주문_테이블_아이디_목록을_조회한다() {
        // given
        OrderTables orderTables = createOrderTables();

        // when
        List<Long> result = orderTables.getOrderTableIds();

        // then
        assertThat(result).containsExactly(1L, 2L);
    }

    private OrderTables createOrderTables() {
        return new OrderTables(
                Arrays.asList(
                        new OrderTable(1L),
                        new OrderTable(2L)
                )
        );
    }


}
