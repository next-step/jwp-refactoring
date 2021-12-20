package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @Test
    @DisplayName("단체 지정 정합성 검사")
    void validateAddTables() {
        OrderTable orderTable_1 = OrderTable.of(0, true);
        OrderTable orderTable_2 = OrderTable.of(2, false);

        OrderTables orderTables = new OrderTables();

        assertThatThrownBy(() -> orderTables.validateAddTables(Arrays.asList(orderTable_1, orderTable_2)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("빈 테이블만 단체지정이 가능합니다.");

        assertThatThrownBy(() -> orderTables.validateAddTables(Arrays.asList(orderTable_1)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("두 테이블 이상이어야 단체지정이 가능합니다.");

        orderTable_2.setEmpty(true);
        orderTable_2.setTableGroup(TableGroup.create());

        assertThatThrownBy(() -> orderTables.validateAddTables(Arrays.asList(orderTable_1, orderTable_2)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("다른 단체에 속한 테이블이 있습니다.");
    }

}