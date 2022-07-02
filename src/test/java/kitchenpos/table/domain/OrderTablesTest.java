package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import kitchenpos.Exception.OrderTableAlreadyEmptyException;
import kitchenpos.Exception.TableGroupSizeException;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTablesTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(NumberOfGuests.from(5), false);
        orderTable2 = new OrderTable(NumberOfGuests.from(10), false);
        orderTables = Arrays.asList(orderTable1, orderTable2);
    }

    @Test
    void 테이블_개수_2개_미만_예외() {
        assertThatThrownBy(
                () -> OrderTables.from(Arrays.asList(orderTable1))
        ).isInstanceOf(TableGroupSizeException.class);
    }

    @Test
    void 빈_테이블_존재_예외() {
        orderTable1.changeEmpty(true);
        assertThatThrownBy(
                () -> OrderTables.from(orderTables)
        ).isInstanceOf(OrderTableAlreadyEmptyException.class);
    }

    @Test
    void 이미_단체_지정_된_테이블_존재_예외() {
        orderTable1.groupByTableGroupId(1L);
        assertThatThrownBy(
                () -> OrderTables.from(orderTables)
        ).isInstanceOf(OrderTableAlreadyEmptyException.class);
    }

    @Test
    void 단체_지정() {
        // given
        orderTable1.changeEmpty(true);
        orderTable2.changeEmpty(true);

        // when
        OrderTables.from(orderTables).group(1L);

        // then
        assertAll(
                () -> assertThat(orderTable1.isGroupedByTableGroup()).isEqualTo(true),
                () -> assertThat(orderTable2.isGroupedByTableGroup()).isEqualTo(true)
        );
    }

    @Test
    void 단체_삭제() {
        // given
        orderTable1.changeEmpty(true);
        orderTable2.changeEmpty(true);
        OrderTables given = OrderTables.from(orderTables);
        given.group(1L);

        // when
        given.unGroup();

        // then
        assertAll(
                () -> assertThat(orderTable1.isGroupedByTableGroup()).isEqualTo(false),
                () -> assertThat(orderTable2.isGroupedByTableGroup()).isEqualTo(false)
        );
    }
}
