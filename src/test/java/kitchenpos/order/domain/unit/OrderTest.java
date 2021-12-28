package kitchenpos.order.domain.unit;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;

import kitchenpos.common.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.domain.Order;

@DisplayName("주문 관련(단위)")
class OrderTest {

    @DisplayName("주문 생성하기")
    @Test
    void createTest() {
        assertThat(Order.from(주문테이블_4명)).isInstanceOf(Order.class);
    }

    @DisplayName("없는 주문을 생성하면 실패함")
    @Test
    void exceptionTest1() {
        assertThatThrownBy(
            () -> Order.from(null)
        ).isInstanceOf(WrongValueException.class);
    }

    @DisplayName("주문완료 상태인 주문의 상태를 변경시도하면 실패함")
    @Test
    void exceptionTest2() {
        Order completedOrder = Order.from(주문테이블_4명);
        completedOrder.changeOrderStatus(OrderStatus.COMPLETION);
        assertThatThrownBy(
            () -> completedOrder.changeOrderStatus(OrderStatus.COOKING)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
