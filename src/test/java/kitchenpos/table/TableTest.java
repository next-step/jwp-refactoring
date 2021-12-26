package kitchenpos.table;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.CannotChangeTableEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableTest {

    @Test
    @DisplayName("사용여부를 변경하고자 하는 테이블이 단체 지정 되어 있다면 예외가 발생한다.")
    void isTableGroup() {
        // given
        final OrderTable orderTable = OrderTable.builder()
                .empty(false)
                .build();
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
        final OrderTable orderTable = OrderTable.builder()
                .empty(false)
                .build();
        orderTable.addOrder(new Order(OrderStatus.COOKING));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderTable.ungroup();
        });
    }
}
