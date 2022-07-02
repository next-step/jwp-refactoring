package kitchenpos.table_group.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(1L);
    }

    @DisplayName("테이블을 그룹핑한다.")
    @Test
    void group() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, true);

        //when
        tableGroup.group(Arrays.asList(orderTable1, orderTable2));

        //then
        그룹핑_확인(tableGroup, orderTable1, orderTable2);
    }

    @DisplayName("테이블이 2개 미만이면 그룹핑할 수 없다.")
    @Test
    void group_fail_low2() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.group(Arrays.asList(orderTable1)));
    }

    @DisplayName("비지 테이블이 있으면 그룹핑할 수 없다.")
    @Test
    void group_fail_notEmptyTable() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, false);

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroup.group(Arrays.asList(orderTable1, orderTable2)));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, true);

        //when
        tableGroup.group(Arrays.asList(orderTable1, orderTable2));

        //then
        그룹핑_확인(tableGroup, orderTable1, orderTable2);

        //when
        tableGroup.ungroup();

        //then
        그룹해제_확인(tableGroup, orderTable1, orderTable2);
    }

    private void 그룹해제_확인(TableGroup tableGroup, OrderTable orderTable1, OrderTable orderTable2) {
        assertEquals(0, tableGroup.getOrderTables().size());
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    private void 그룹핑_확인(TableGroup tableGroup, OrderTable orderTable1, OrderTable orderTable2) {
        assertEquals(2, tableGroup.getOrderTables().size());
        assertEquals(tableGroup.getId(), orderTable1.getTableGroupId());
        assertEquals(tableGroup.getId(), orderTable2.getTableGroupId());
    }

}