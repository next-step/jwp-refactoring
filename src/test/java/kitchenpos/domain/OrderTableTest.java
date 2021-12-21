package kitchenpos.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;

public class OrderTableTest {
    @DisplayName("손님의 수를 변경한다")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4})
    void testChangeNumberOfGuest(int numberOfGuest) {
        // given
        OrderTable orderTable = new OrderTable();

        // when
        orderTable.changeNumberOfGuests(numberOfGuest);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuest);
    }

    @DisplayName("손님의 수는 빈 테이블이 아닐 때 변경 할 수 있다")
    @Test
    void testCantChangeNumberOfGuest() {
        // given
        OrderTable orderTable = new OrderTable(anyInt(), true);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> orderTable.changeNumberOfGuests(anyInt());

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
