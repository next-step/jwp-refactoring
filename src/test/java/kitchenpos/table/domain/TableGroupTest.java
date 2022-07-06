package kitchenpos.table.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupTest {
    @Test
    @DisplayName("테이블 그룹 생성")
    void create() {
        // given
        final List<OrderTable> savedOrderTables = Arrays.asList(OrderTable.of(0, true), OrderTable.of(0, true));
        final List<OrderTable> orderTables = Arrays.asList(OrderTable.of(0, true), OrderTable.of(0, true));
        // when
        final TableGroup save = TableGroup.of(orderTables, savedOrderTables.size());
        // then
        assertThat(save).isInstanceOf(TableGroup.class);
    }
}
