package kitchenpos.table;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.exception.CannotChangeTableEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableTest {

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 단체 지정 되어 있다면 예외가 발생한다.")
    void isTableGroup() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);
        orderTable.addTableGroup(TableGroup.builder().build());

        // when
        assertThatThrownBy(() -> {
            orderTable.changeEmpty(true);
        }).isInstanceOf(CannotChangeTableEmptyException.class);
    }

    @Test
    @DisplayName("그룹해제시 주문상태가 조리 또는 식사면 예외가 발생한다.")
    void orderStatusCookingOrMeal() {
        // given
        final OrderTable orderTable = new OrderTable(0, false);
        orderTable.addOrder(new Order(OrderStatus.COOKING));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderTable.ungroup();
        });
    }
}
