package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.exception.NegativeNumberOfGuestsException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @DisplayName("OrderTable를 생성할 수 있다. (NumberOfGuests, isEmpty)")
    @Test
    void create01() {
        // given & when & then
        assertThatNoException().isThrownBy(() -> OrderTable.of(true, 1));
    }

    @DisplayName("OrderTable 생성 시 NumberofGuest가 양수가 아닌 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void create02(int numberOfGuests) {
        // when & then
        assertThrows(NegativeNumberOfGuestsException.class, () -> OrderTable.of(true, numberOfGuests));
    }

    @DisplayName("OrderTable의 NumberOfGuests를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {2, 10, 100})
    void change01(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.of(false, 1);

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertEquals(numberOfGuests, orderTable.findNumberOfGuests());
    }

    @DisplayName("OrderTable의 NumberOfGuests를 양수가 아닌경우 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = {-1000, -10, -1})
    void change02(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTable.of(false, 1);

        // when & then
        assertThrows(NegativeNumberOfGuestsException.class, () -> orderTable.changeNumberOfGuests(numberOfGuests));
    }

    @DisplayName("OrderTable의 empty 상태를 변경할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void change03(boolean empty) {
        // given
        OrderTable orderTable = OrderTable.of(false, 1);

        // when
        orderTable.changeEmpty(empty);

        // then
        assertEquals(empty, orderTable.isEmpty());
    }

    @DisplayName("OrderTable을 TableGroup에 할당할 수 있다.")
    @Test
    void align01() {
        // given
        OrderTable orderTable = OrderTable.of(true, 1);
        TableGroup tableGroup = TableGroup.create();

        // when
        tableGroup.group(Lists.newArrayList(orderTable.getId()));

        // then
        assertEquals(tableGroup.getId(), orderTable.getTableGroupId());
    }
}