package kitchenpos.table.domain;

import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTablesTest {

    @Test
    @DisplayName("주문테이블에 단체테이블을 지정한다")
    void addTableGroupAndEmpties() {
        //  given
        OrderTables orderTables = new OrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        TableGroup tableGroup = new TableGroup();

        // when
        orderTables.addTableGroup(tableGroup);

        // then
        assertAll(
                () -> assertThat(orderTables.values().get(0).getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(orderTables.values().get(1).getTableGroup()).isEqualTo(tableGroup)
        );
    }
}
