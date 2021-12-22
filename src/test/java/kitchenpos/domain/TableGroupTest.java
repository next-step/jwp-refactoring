package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TableGroupTest {
    @Test
    void changeOrderTablesToNotEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(1, true), new OrderTable(2, true)));

        // when
        tableGroup.changeOrderTablesToNotEmpty();

        // then
        assertThat(tableGroup.getOrderTables()).allMatch(table -> !table.isEmpty());
    }

    @Test
    void ungroup() {
        // given
        final TableGroup tableGroup =
            new TableGroup(Arrays.asList(new OrderTable(1L, 1, true), new OrderTable(1L, 2, true)));

        // when
        tableGroup.ungroup();

        // then
        assertThat(tableGroup.getOrderTables()).allMatch(table -> table.getTableGroupId() == null);
    }
}
