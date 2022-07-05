package kitchenpos.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.domain.OrderTablesTest.createGroupingOrderTables;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupTest {

    @Test
    void 주문테이블을_변경한다() {
        // given
        TableGroup tableGroup = createTableGroup();
        List<OrderTable> orderTables = createGroupingOrderTables().elements();

        // when
        tableGroup.changeOrderTables(orderTables);

        // then
        assertThat(tableGroup.getOrderTables()).isEqualTo(orderTables);
    }

    public static TableGroup createTableGroup() {
        return new TableGroup(Arrays.asList(
                new OrderTable(2L),
                new OrderTable(3L)
        ));
    }
}
