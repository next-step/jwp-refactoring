package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static kitchenpos.order.OrderGenerator.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    private Order 주문 = spy(주문_생성(0L, 주문_물품_목록_생성(주문_물품_생성(0L, 0L))));

    @DisplayName("완료 상태가 아닌 주문의 상태 변경 가능여부를 확인하면 예외가 발생하지 않아야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL" })
    void orderChangeStatusTest(OrderStatus orderStatus) {
        // given
        when(주문.getOrderStatus()).thenReturn(orderStatus);

        // then
        assertThatNoException().isThrownBy(() -> orderValidator.isPossibleChangeOrderStatus(주문));
    }

    @DisplayName("완료 상태인 주문의 상태 변경 가능여부를 확인하면 예외가 발생해야 한다")
    @Test
    void orderChangeStatusByCompletionStateTest() {
        // given
        when(주문.getOrderStatus()).thenReturn(OrderStatus.COMPLETION);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.isPossibleChangeOrderStatus(주문));
    }
}
