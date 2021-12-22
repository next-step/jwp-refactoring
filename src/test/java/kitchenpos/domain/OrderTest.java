package kitchenpos.domain;

import kitchenpos.exception.CannotProgressException;
import kitchenpos.exception.NoOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 테이블 없이 주문을 생성한다")
    @Test
    void noTableTest() {
        assertThatThrownBy(() -> Order.of(null, OrderStatus.COOKING)).isInstanceOf(NoOrderTableException.class);
    }

    @DisplayName("결제완료 상태인 주문상태를 변경한다")
    @Test
    void cannotProgressExceptionTest() {
        Order order = Order.of(new OrderTable(), OrderStatus.COMPLETION);
        assertThatThrownBy(() -> order.makeOrderStatus(OrderStatus.COOKING)).isInstanceOf(CannotProgressException.class);
    }
}