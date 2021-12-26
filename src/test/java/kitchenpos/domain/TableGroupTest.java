package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    @DisplayName("주문 테이블 그룹화")
    public void toGroup() {
        //given
        TableGroup tableGroup = new TableGroup();
        OrderTables orderTables = new OrderTables(Arrays.asList(
            new OrderTable(1L, 5, true),
            new OrderTable(2L, 3, true)
        ));

        //when
        orderTables.toGroup(tableGroup);

        //then
        assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNotNull();
        assertThat(tableGroup.getOrderTables().getOrderTables()).containsAll(
            orderTables.getOrderTables());
    }

    @Test
    @DisplayName("주문 테이블 그룹해제")
    public void ungroup() {
        //given
        TableGroup tableGroup = new TableGroup();
        OrderTables orderTables = new OrderTables(Arrays.asList(
            new OrderTable(1L, 5, true),
            new OrderTable(2L, 3, true)
        ));
        orderTables.toGroup(tableGroup);

        //when
        tableGroup.ungroup();

        //then
        assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNull();
        assertThat(orderTables.getOrderTables().get(1).getTableGroup()).isNull();
    }
}
