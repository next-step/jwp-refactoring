package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTablesTest {

    @DisplayName("테이블이 두 개 미만일 때 그룹을 하는 경우 예외 발생")
    @Test
    void 테이블_그룹_예외1() {
        // given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTables orderTables = OrderTables.of(Lists.newArrayList(orderTable1));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderTables.validateForCreatableGroup()
            )
            .withMessage("테이블이 두 개 이상 있어야 합니다");
    }

    @DisplayName("비어있지 않은 테이블을 그룹하는 경우 예외 발생")
    @Test
    void 테이블_그룹_예외2() {
        // given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(4, false);
        OrderTables orderTables = OrderTables.of(Lists.newArrayList(orderTable1, orderTable2));
        OrderTables savedOrderTables = OrderTables.of(Lists.newArrayList(orderTable1, orderTable2));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
                () -> savedOrderTables.validateForCreatableGroup(orderTables)
            )
            .withMessage("테이블이 그룹할 수 없는 상태입니다");
    }
    
}