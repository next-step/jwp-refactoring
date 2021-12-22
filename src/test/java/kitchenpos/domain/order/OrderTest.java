package kitchenpos.domain.order;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.InvalidParameterException;
import java.util.Collections;
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
        Order 주문 = Order.of(손님있는_테이블, Collections.singletonList(주문항목));

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("`주문 항목`은 필수 이다.")
    void 주문항목_없는_경우_실패() {
        // given
        OrderTable 손님있는_테이블 = OrderTable.of(1, false);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> Order.of(손님있는_테이블, Collections.emptyList());

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`은 `빈 테이블`상태가 아니어야 한다.")
    void 주문에_속할_주문테이블이_빈테이블_아니면_실패() {
        // given
        OrderTable 빈테이블 = OrderTable.of(0, true);
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> Order.of(빈테이블,
            Collections.singletonList(주문항목));

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 상태`가 `계산 완료`이면 상태를 변경 할 수 없다.")
    void 주문상태가_계산완료_이면_상태_변경_실패() {
        // given
        OrderTable 손님있는_테이블 = OrderTable.of(1, false);
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(손님있는_테이블, Collections.singletonList(주문항목));
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}