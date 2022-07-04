package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GroupOrderTablesTest {

    @Test
    @DisplayName("단체지정 테이블은 테이블 2개부터 지정이 가능하다.")
    void createTableGroup() {
        //given
        OrderTable orderTable = new OrderTable(3, false);

        //when & then
        assertAll(
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> OrderTables.of(null)),
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> OrderTables.of(Collections.emptyList())),
                () -> assertThatIllegalArgumentException()
                        .isThrownBy(() -> OrderTables.of(Collections.singletonList(orderTable)))
        );
    }

    @Test
    @DisplayName("단체지정 테이블 대상중 빈테이블이 있을 경우 단체석을 지정 할 수 없음")
    void hasEmptyTable() {
        //given
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, true);
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable, orderTable2));


        assertThatIllegalArgumentException()
                .isThrownBy(()-> orderTables.group(1L));
    }

    @Test
    @DisplayName("단체지정 테이블 대상중 이미 단체테이블에 지정되어 있을 경우 단체석을 지정 할 수 없음")
    void hasTableGroupEmptyTable() {
        //given
        OrderTable orderTable = new OrderTable(1L, 1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        OrderTables orderTables = OrderTables.of(Arrays.asList(orderTable, orderTable2));

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(()-> orderTables.group(1L));
    }

    @Test
    @DisplayName("단체지정 테이블을 지정")
    void group() {
        //given
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        OrderTables groupOrderTables = OrderTables.of(Arrays.asList(orderTable, orderTable2));

        //when
        groupOrderTables.group(1L);

        //then
        assertThat(groupOrderTables.orderTables)
                .extracting("tableGroupId").containsExactly(1L, 1L);
    }

    @Test
    @DisplayName("개인 테이블을 지정")
    void unGroup() {
        //given
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        OrderTables groupOrderTables = OrderTables.of(Arrays.asList(orderTable, orderTable2));

        //when
        groupOrderTables.group(1L);
        groupOrderTables.unGroup();

        //then
        assertThat(groupOrderTables.orderTables)
                .extracting("tableGroupId").containsExactly(null, null);
    }
}
