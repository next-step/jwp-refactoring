package kitchenpos.domain;

import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("주문 일급 컬렉션 테스트")
class OrdersTest {

    private OrderTable orderTable = OrderTable.of(10, false);
    private Order order1 = Order.of(1L, orderTable, Arrays.asList(OrderLineItem.of(1L, 2)));
    private Order order2 = Order.of(2L, orderTable, Arrays.asList(OrderLineItem.of(2L, 2)));
    private Orders orders = Orders.from(Arrays.asList(order1, order2));

    @DisplayName("특정 주문 상태를 포함하는지 확인한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"COOKING:true", "MEAL:true", "COMPLETION:false"}, delimiter = ':')
    void anyMatchedIn(OrderStatus input, boolean expected) {
        order1.changeOrderStatus(OrderStatus.COOKING);
        order2.changeOrderStatus(OrderStatus.MEAL);

        Assertions.assertThat(orders.anyMatchedIn(Arrays.asList(input))).isEqualTo(expected);
    }
}
