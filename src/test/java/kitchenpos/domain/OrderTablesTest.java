package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.domain.OrderTableTest.EMPTY_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    void 두개_미만의_테이블은_단체지정을_할_수_없다() {
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

    @Test
    void 중복되거나_등록되지_않은_테이블은_단체지정을_할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                createOrderTables().validateTablesSize(1)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 테이블이 있거나 등록되지 않은 테이블이 있습니다.");
    }

    @Test
    void 빈_테이블이_아니거나_이미_단체가_지정되었으면_단체지정을_할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                createEmptyOrderTables().validateCanGroup()
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블이 아니거나 이미 단체가 지정되었습니다.");
    }

    public static OrderTables createOrderTables() {
        return new OrderTables(
                Arrays.asList(
                        new OrderTable(1L),
                        new OrderTable(2L)
                )
        );
    }

    private OrderTables createEmptyOrderTables() {
        return new OrderTables(
                Arrays.asList(
                        EMPTY_TABLE,
                        new OrderTable(2L)
                )
        );
    }

    public static OrderTables createGroupingOrderTables() {
        return new OrderTables(
                Arrays.asList(
                        new OrderTable(1, true),
                        new OrderTable(2, true)
                )
        );
    }
}
