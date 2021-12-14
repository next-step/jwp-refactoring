package kitchenpos.order.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 도메인 테스트")
class OrderTest {

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"COOKING:false", "MEAL:false", "COMPLETION:true"}, delimiter = ':')
    @DisplayName("변경 가능 여부를 반환한다.")
    void isChangable(OrderStatus orderStatus, boolean expected) {
        // given
        Order order = new Order(orderStatus);

        // when
        boolean changable = order.isChangable();

        // then
        assertThat(changable).isEqualTo(expected);
    }

    @Test
    @DisplayName("비어있는 테이블로 주문의 테이블을 변경하면 예외를 발생한다.")
    void changeOrderTableThrowException() {
        // given
        OrderTable orderTable = new OrderTable(true);
        Order order = new Order(OrderStatus.MEAL);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> order.changeOrderTable(orderTable));
    }

    @Test
    @DisplayName("주문 완료 상인 주문의 주문 상태를 변경하면 예외를 발생한다.")
    void changeOrderStatusThrowException() {
        // given
        Order order = new Order(OrderStatus.COMPLETION);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}
