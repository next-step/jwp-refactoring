package kitchenpos.domain.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.stream.Stream;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:OrderTable")
class OrderTableTest {

    @ParameterizedTest(name = "case[{index}] : {0} => {1}")
    @MethodSource
    @DisplayName("주문 상태 변경")
    public void changeEmpty(final boolean given, final boolean expected) {
        // Given
        OrderTable orderTable = new OrderTable(0, given);

        // When
        orderTable.changeEmpty(expected);

        // Then
        assertThat(orderTable.isEmpty()).isEqualTo(expected);
    }

    private static Stream<Arguments> changeEmpty() {
        return Stream.of(
            Arguments.of(true, false),
            Arguments.of(false, true)
        );
    }

    @Test
    @DisplayName("변경 대상 주문 테이블이 단체 지정된 테이블인 경우 예외 발생 검증")
    public void throwException_WhenTargetOrderTableIsGrouped() {
        // Given
        OrderTable orderTable = new OrderTable(0, true);
        TableGroup.of(Collections.singletonList(orderTable.getId()), Collections.singletonList(orderTable));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.changeEmpty(true));
    }

    @Test
    @DisplayName("주문 객수 변경")
    public void changeNumberOfGuests() {
        // Given
        final int newNumberOfGuests = 3;
        OrderTable orderTable = new OrderTable(0, false);

        // When
        orderTable.changeNumberOfGuests(newNumberOfGuests);

        // Then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
    }

    @Test
    @DisplayName("변경 대상 주문 테이블이 비어있는 경우 예외 발생 검증")
    public void throwException_WhenTargetOrderTableIsEmpty() {
        // Given
        final int newNumberOfGuests = 3;
        OrderTable orderTable = new OrderTable(0, true);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.changeNumberOfGuests(newNumberOfGuests));
    }

    @Test
    @DisplayName("변경하는 객수가 음수인 경우 에외 발생 검증")
    public void throwException_WhenRequestNumberOfGuestsIsInvalid() {
        // Given
        OrderTable orderTable = new OrderTable(0, false);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderTable.changeNumberOfGuests(Integer.MIN_VALUE));
    }
}
