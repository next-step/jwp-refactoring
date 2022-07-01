package kitchenpos.table.domain;

import kitchenpos.embeddableEntity.Empty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {

    @Test
    @DisplayName("주문테이블에 단체테이블을 지정한다")
    void addTableGroupAndEmpties() {
        //  given
        OrderTables orderTables = new OrderTables(Arrays.asList(new OrderTable(), new OrderTable()));
        TableGroup tableGroup = new TableGroup();
        Empty empty = new Empty(false);

        // when
        orderTables.addTableGroupAndEmpties(empty.value(), tableGroup);

        // then
        assertAll(
                () -> assertThat(orderTables.values().get(0).getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(orderTables.values().get(1).getTableGroup()).isEqualTo(tableGroup),
                () -> assertThat(orderTables.values().get(0).getEmpty()).isEqualTo(empty),
                () -> assertThat(orderTables.values().get(1).getEmpty()).isEqualTo(empty)
        );
    }
}
