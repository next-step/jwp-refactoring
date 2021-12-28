package kitchenpos.order.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import kitchenpos.exception.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Order 클래스")
@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private OrderValidator orderValidator;

    @Test
    @DisplayName("`주문` 최초 상태는 `조리`다.")
    void 주문초기_상태는_조리() {
        // given
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);

        // when
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("`주문 항목`은 필수 이다.")
    void 주문항목_없는_경우_실패() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> Order.of(1L, Collections.emptyList());

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 상태`가 `계산 완료`이면 상태를 변경 할 수 없다.")
    void 주문상태가_계산완료_이면_상태_변경_실패() {
        // given
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("registerOrder 는 주문생성검증을 호출 해야한다.")
    void registerOrder() {
        // given
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));

        // when
        주문.registerOrder(orderValidator);

        // then
        verify(orderValidator).validateRegister(주문);
    }
}
