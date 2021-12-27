package kitchenpos.order.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Order 클래스")
class OrderTest {

    @Test
    @DisplayName("`주문` 최초 상태는 `조리`다.")
    void 주문초기_상태는_조리() {
        // given
        OrderTable 손님있는_테이블 = OrderTable.of(1, false);
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);

        // when
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("`주문 항목`은 필수 이다.")
    void 주문항목_없는_경우_실패() {
        // given
        OrderTable 손님있는_테이블 = OrderTable.of(1, false);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> Order.of(1L, Collections.emptyList());

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 상태`가 `계산 완료`이면 상태를 변경 할 수 없다.")
    void 주문상태가_계산완료_이면_상태_변경_실패() {
        // given
        OrderTable 손님있는_테이블 = OrderTable.of(1, false);
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
