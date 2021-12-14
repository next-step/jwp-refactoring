package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("주문 항목 컬렉션 테스트")
class OrdersTest {

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
