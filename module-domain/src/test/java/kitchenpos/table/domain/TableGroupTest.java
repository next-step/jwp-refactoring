package kitchenpos.table.domain;

import kitchenpos.exception.AlreadyExistTableGroupException;
import kitchenpos.exception.InvalidMinOrderTableSizeException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class TableGroupTest {

    @DisplayName("단체 지정의 주문 테이블 목록이 올바르지 않으면 등록할 수 없다 : 주문 테이블 목록은 2개 이상이어야 한다.")
    @Test
    void createTest_orderTablesSize_lessThanTwo() {
        // when & then
        assertThatThrownBy(() -> new TableGroup(new ArrayList<>()))
                .isInstanceOf(InvalidMinOrderTableSizeException.class);
    }

    @DisplayName("단체 지정의 주문 테이블 목록이 올바르지 않으면 등록할 수 없다 : 주문 테이블은 이미 다른 단체 지정에 등록되어있지 않아야 한다.")
    @Test
    void createTest_wrongOrderTable() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, false);
        OrderTable orderTable2 = new OrderTable(2L, 1, false);
        new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(orderTable1, orderTable2)))
                .isInstanceOf(AlreadyExistTableGroupException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroupTest() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, false);
        OrderTable orderTable2 = new OrderTable(2L, 1, false);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        // when
        tableGroup.ungroup();

        // then
        assertThat(tableGroup.getOrderTables()).isEmpty();
    }
}
