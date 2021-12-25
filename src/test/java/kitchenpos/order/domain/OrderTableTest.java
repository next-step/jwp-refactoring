package kitchenpos.order.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;

public class OrderTableTest {
    private OrderTableValidator orderTableValidator;

    @BeforeEach
    void setUp() {
        orderTableValidator = Mockito.mock(OrderTableValidator.class);
    }

    @DisplayName("손님의 수를 변경한다")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4})
    void testChangeNumberOfGuest(int numberOfGuest) {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        orderTable.changeNumberOfGuests(numberOfGuest, orderTableValidator);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuest);
    }

    @DisplayName("손님의 수는 빈 테이블이 아닐 때 변경 할 수 있다")
    @Test
    void testCantChangeNumberOfGuest() {
        // given
        OrderTable orderTable = new OrderTable(3, true);
        doThrow(IllegalArgumentException.class).when(orderTableValidator).validateChangeNumberOfGuests(orderTable);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTable.changeNumberOfGuests(orderTable.getNumberOfGuests(), orderTableValidator);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 비움 상태를 변경한다")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testChangeEmpty(boolean expectedEmpty) {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        orderTable.changeEmptyStatus(expectedEmpty, orderTableValidator);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(expectedEmpty);
    }

    @DisplayName("조리중, 식사중인 테이블을 비울 수 없다")
    @Test
    void whenOrderedThenCanNotChangeEmpty() {
        // given
        OrderTable orderTable = new OrderTable();
        doThrow(IllegalArgumentException.class).when(orderTableValidator).validateHasProgressOrder(orderTable);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTable.changeEmptyStatus(true, orderTableValidator);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
