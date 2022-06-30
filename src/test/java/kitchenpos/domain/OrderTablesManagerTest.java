package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import kitchenpos.exception.TableGroupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesManagerTest {

    private OrderTablesManager orderTablesManager;
    private OrderTable orderTable_1;
    private OrderTable orderTable_2;

    @BeforeEach
    public void init() {
        orderTablesManager = new OrderTablesManager();
        orderTable_1 = new OrderTable(0, true);
        orderTable_2 = new OrderTable(0, true);
    }

    @Test
    @DisplayName("1개 이하의 테이블로 그룹을 설정하면 에러가 발생한다")
    public void groupWithUnder1TableThrowErrorTest() {
        //when & then
        assertAll(
            () -> assertThatThrownBy(
                () -> orderTablesManager.mapOrderTables(Arrays.asList(orderTable_1)))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("단체테이블은 2개 이상이여야 합니다"),
            () -> assertThatThrownBy(() -> orderTablesManager.mapOrderTables(Arrays.asList()))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("단체테이블은 2개 이상이여야 합니다")
        );
    }

    @Test
    @DisplayName("사용중인 테이블은 그룹을 설정할수 없다")
    public void groupWithAlreadyUsingTableThrowErrorTest() {
        //given
        orderTable_1.useTable();

        //when & then
        assertThatThrownBy(
            () -> orderTablesManager.mapOrderTables(Arrays.asList(orderTable_1, orderTable_2)))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("단체테이블은 2개 이상이여야 합니다");
    }

    @Test
    @DisplayName("이미 그룹이 설정된 테이블을 그룹설정할수 없다")
    public void groupWithAlreadyGroupedTableThrowErrorTest() {
        //given
        orderTable_1.mapToTableGroup(new TableGroup());

        //when & then
        assertThatThrownBy(
            () -> orderTablesManager.mapOrderTables(Arrays.asList(orderTable_1, orderTable_2)))
            .isInstanceOf(TableGroupException.class)
            .hasMessage("단체테이블은 2개 이상이여야 합니다");
    }
}