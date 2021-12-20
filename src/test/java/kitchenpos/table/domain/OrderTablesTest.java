package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.exception.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 목록 일급 컬렉션 테스트")
class OrderTablesTest {

    @DisplayName("빈테이블인 경우 단체 지정 불가")
    @Test
    void validateAddNotEmptyTable() {
        OrderTable orderTable_1 = OrderTable.of(0, true);
        OrderTable orderTable_2 = OrderTable.of(2, false);

        OrderTables orderTables = new OrderTables();

        assertThatThrownBy(() -> orderTables.validateAddTables(Arrays.asList(orderTable_1, orderTable_2)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("빈 테이블만 단체지정이 가능합니다.");
    }

    @DisplayName("두 테이블 이상 단체 지정 가능")
    @Test
    void validateAddMinSizeTable() {
        OrderTables orderTables = new OrderTables();
        assertThatThrownBy(() -> orderTables.validateAddTables(Arrays.asList(OrderTable.of(0, true))))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("두 테이블 이상이어야 단체지정이 가능합니다.");
    }

    @DisplayName("다른 단체에 속한 테이블은 단체지정할 수 없음")
    @Test
    void validateAddOtherTableGroup() {
        OrderTable orderTable_1 = OrderTable.of(0, true);
        OrderTable orderTable_2 = OrderTable.of(2, true);
        orderTable_2.setTableGroup(TableGroup.create());

        OrderTables orderTables = new OrderTables();

        assertThatThrownBy(() -> orderTables.validateAddTables(Arrays.asList(orderTable_1, orderTable_2)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("다른 단체에 속한 테이블이 있습니다.");
    }

}