package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 항목 컬렉션 테스트")
class OrdersTest {

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"COOKING", "MEAL"}, delimiter = ':')
    @DisplayName("주문 완료 상태가 아닌 주문으로 테이블 상태 변경 가능여부를 검증하면 예외를 발생한다.")
    void validateEmptyChangableThrowException(OrderStatus orderStatus) {
        // given
        Orders orders = new Orders(Collections.singletonList(new Order(orderStatus)));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(orders::validateEmptyChangable)
                .withMessageMatching(Orders.MESSAGE_VALIDATE_EMPTY_CHANGABLE);
    }

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"COOKING:false", "MEAL:false", "COMPLETION:true"}, delimiter = ':')
    @DisplayName("변경 가능 여부를 반환한다.")
    void isChangable(OrderStatus orderStatus, boolean expected) {
        // given
        Orders orders = new Orders(Arrays.asList(new Order(OrderStatus.COMPLETION), new Order(orderStatus)));

        // when
        boolean changable = orders.isChangable();

        // then
        assertThat(changable).isEqualTo(expected);
    }
}
