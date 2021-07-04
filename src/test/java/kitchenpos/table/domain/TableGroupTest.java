package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupTest {

    private Long tableGroup1Id = 1L;

    @BeforeEach
    void setUp() {

    }

    @Test
    void create() {
        OrderTable orderTable1 = new OrderTable(null, 0, true);
        OrderTable orderTable2 = new OrderTable(null, 0, true);

        TableGroup tableGroup = new TableGroup(tableGroup1Id, Arrays.asList(orderTable1, orderTable2));
        tableGroup.grouping();

        List<OrderTable> orderTables = tableGroup.getOrderTables();
        assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(tableGroup1Id);
        assertThat(orderTables.get(0).isEmpty()).isFalse();
    }

    @Test
    void grouping() {
        OrderTable orderTable1 = new OrderTable(null, 0, true);
        OrderTable orderTable2 = new OrderTable(null, 0, true);

        TableGroup tableGroup = new TableGroup(tableGroup1Id, Arrays.asList(orderTable1, orderTable2));
        tableGroup.grouping();
    }

}
