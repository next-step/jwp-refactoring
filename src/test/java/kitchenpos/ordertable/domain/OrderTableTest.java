package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 도메인 테스트")
class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(false);
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() {
        // given
        Order order = new Order(OrderStatus.COMPLETION);
        order.changeOrderTable(orderTable);

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(new Empty(true));
    }

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"COOKING", "MEAL"}, delimiter = ':')
    @DisplayName("주문 완료 상태가 아닌 테이블의 상태를 변경하면 예외를 발생한다.")
    void changeEmptyThrowException(OrderStatus orderStatus) {
        // given
        Order order = new Order(orderStatus);
        order.changeOrderTable(orderTable);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderTable.changeEmpty(true));
    }

    @Test
    @DisplayName("방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // when
        orderTable.changeNumberOfGuests(1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(1));
    }

    @Test
    @DisplayName("0명 이하의 손님 수로 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException1() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderTable.changeNumberOfGuests(-1));
    }

    @Test
    @DisplayName("비어있는 테이블의 방문한 손님 수를 변경하면 예외를 발생한다.")
    void changeNumberOfGuestsThrowException2() {
        // given
        orderTable.changeEmpty(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> orderTable.changeNumberOfGuests(1));
    }
}
