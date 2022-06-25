package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Test
    @DisplayName("주문 테이블이 2개보다 작으면 테이블 그룹으로 지정 할 수 없다.")
    void createFailWithEmptyTest() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, false);

        //when & then
        Assertions.assertAll(
                () -> assertThatThrownBy(
                        () -> new TableGroup(1L, Collections.emptyList())
                ).isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(
                        () -> new TableGroup(1L,  Arrays.asList(orderTable1))
                ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속해 있으면 테이블 그룹을 지정 할 수 없다.")
    void createFailWithTableGroupTest() {
        //given
        TableGroup otherTableGroup = new TableGroup(1L);
        TableGroup tableGroup = new TableGroup(2L);
        OrderTable orderTable1 = new OrderTable(1L, otherTableGroup, true);
        OrderTable orderTable2 = new OrderTable(2L, otherTableGroup, true);

        //when & then
        assertThatThrownBy(
                () -> tableGroup.addOrderTables(Arrays.asList(orderTable1, orderTable2))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈테이블이 아니면 테이블 그룹을 지정 할 수 없다.")
    void createFailWithEmptyTableTest() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, false);
        OrderTable orderTable2 = new OrderTable(2L, false);

        //when & then
        assertThatThrownBy(
                () -> tableGroup.addOrderTables(Arrays.asList(orderTable1, orderTable2))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해지한다.")
    void ungroupTest() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, true);
        OrderTable orderTable2 = new OrderTable(2L, true);
        tableGroup.addOrderTables(Arrays.asList(orderTable1, orderTable2));

        //when & then
        tableGroup.ungroup();
        assertThat(tableGroup.getOrderTables()).isEmpty();
    }
}
