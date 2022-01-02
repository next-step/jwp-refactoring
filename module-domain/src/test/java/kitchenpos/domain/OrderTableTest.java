package kitchenpos.domain;

import kitchenpos.order.exception.NegativeNumberOfGuestsException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderValidator;
import kitchenpos.table.exception.CannotChangeEmptyException;
import kitchenpos.table.exception.CannotChangeNumberOfGuestsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    OrderValidator orderValidator;

    @DisplayName("빈 테이블로 변경한다")
    @Test
    void changeEmptyTest() {
        orderValidator = new OrderValidatorImplTest();
        OrderTable orderTable = new OrderTable(2L, null, 1, false);

        orderTable.changeEmpty(orderValidator, true);

        assertThat(orderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹에 속해있는 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    public void inTableGroupChangeEmptyTest() {
        OrderTable tableGroupOrderTable = new OrderTable(1L, 1, false);

        assertThatThrownBy(() -> tableGroupOrderTable.changeEmpty(orderValidator, true))
                .isInstanceOf(CannotChangeEmptyException.class);
    }

    @DisplayName("조리 중이거나 식사 중인 테이블은 빈 테이블로 변경할 수 없다")
    @Test
    void changeEmptyTableInCookingOrMeal() {
        orderValidator = new OrderValidatorImplTest();
        OrderTable CookingOrderTable = new OrderTable(1L, null, 1, false);

        assertThatThrownBy(() -> CookingOrderTable.changeEmpty(orderValidator, true))
                .isInstanceOf(CannotChangeEmptyException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 지정한다")
    @Test
    void changeNumberOfGuestsTest() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        orderTable.changeNumberOfGuests(10);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문 테이블의 손님 수를 음수로 지정한다")
    @Test
    void changeNumberOfGuestsNegativeTest() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);

        // then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
                .isInstanceOf(NegativeNumberOfGuestsException.class);
    }

    @DisplayName("빈 테이블의 손님 숫자를 지정할 수 없다")
    @Test
    void changeNumberOfGuestsEmptyTable() {
        // given
        OrderTable emptyOrderTable = new OrderTable(null, 1, true);

        // then
        assertThatThrownBy(() -> emptyOrderTable.changeNumberOfGuests(10))
                .isInstanceOf(CannotChangeNumberOfGuestsException.class);
    }
}
