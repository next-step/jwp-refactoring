package kitchenpos.domain;

import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

    @DisplayName("id가 같은 두 객체는 동등하다.")
    @Test
    void equalsTest() {
        OrderTable table1 = OrderTable.of(1L, 1, false);
        OrderTable table2 = OrderTable.of(1L, 1, false);

        Assertions.assertThat(table1).isEqualTo(table2);
    }

    @DisplayName("id가 다르면 두 객체는 동등하지 않다.")
    @Test
    void equalsTest2() {
        OrderTable table1 = OrderTable.of(1L, 1, false);
        OrderTable table2 = OrderTable.of(2L, 1, false);

        Assertions.assertThat(table1).isNotEqualTo(table2);
    }

    @DisplayName("주문 테이블이 단체 지정이 되어있으면 주문 테이블의 empty 변경 시 예외가 발생한다.")
    @Test
    void changeEmptyException() {
        OrderTable 단체_지정된_주문_테이블 = OrderTable.of(1L, 1L, 10, false);

        boolean empty = !단체_지정된_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> 단체_지정된_주문_테이블.changeEmpty(empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문 상태가 조리이면 주문 테이블의 empty 변경 시 예외가 발생한다.")
    @Test
    void changeEmptyException2() {
        OrderTable 조리상태_주문_테이블 = OrderTable.of(10, false);
        Order.order(조리상태_주문_테이블, Arrays.asList(OrderLineItem.of(1L, 2)));

        boolean empty = !조리상태_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> 조리상태_주문_테이블.changeEmpty(empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문 상태가 식사이면 주문 테이블의 empty 변경 시 예외가 발생한다.")
    @Test
    void changeEmptyException3() {
        OrderTable 식사상태_주문_테이블 = OrderTable.of(10, false);
        Order 주문 = Order.order(식사상태_주문_테이블, Arrays.asList(OrderLineItem.of(1L, 2)));
        주문.setOrderStatus(OrderStatus.MEAL.name());

        boolean empty = !식사상태_주문_테이블.isEmpty();
        Assertions.assertThatThrownBy(() -> 식사상태_주문_테이블.changeEmpty(empty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 empty 상태가 변경된다.")
    @Test
    void changeEmpty() {
        OrderTable 주문_테이블 = OrderTable.of(10, false);
        Order 주문 = Order.order(주문_테이블, Arrays.asList(OrderLineItem.of(1L, 2)));
        주문.setOrderStatus(OrderStatus.COMPLETION.name());

        주문_테이블.changeEmpty(!주문_테이블.isEmpty());

        Assertions.assertThat(주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 방문한 손님 수가 0 미만이면 방문한 손님 수를 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsException() {
        OrderTable 주문_테이블 = OrderTable.of(10, false);

        Assertions.assertThatThrownBy(() -> 주문_테이블.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 방문한 손님 수를 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsException2() {
        OrderTable 주문_테이블 = OrderTable.of(10, true);

        Assertions.assertThatThrownBy(() -> 주문_테이블.changeNumberOfGuests(1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"1:1", "5:5"}, delimiter = ':')
    void changeNumberOfGuests(int input, int expected) {
        OrderTable 주문_테이블 = OrderTable.of(10, false);

        주문_테이블.changeNumberOfGuests(input);

        Assertions.assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(expected);
    }
}
