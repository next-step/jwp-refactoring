package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class TableGroupTest {

    private OrderTable 주문_테이블_2명;

    @BeforeEach
    void setUp() {
        주문_테이블_2명 = OrderTable.of(NumberOfGuests.of(2), Empty.of(true));
    }

    @Test
    @DisplayName("테이블 그룹 생성")
    void tableGroup() {
        // given
        OrderTables tables = OrderTables.of(Arrays.asList(주문_테이블_2명));
        TableGroup tableGroup = TableGroup.of(tables);

        Assertions.assertThat(tableGroup.getOrderTables()).size().isEqualTo(1);
    }
}
