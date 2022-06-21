package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.CreateOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private OrderTable 테이블;

    @BeforeEach
    void setUp() {
        테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
    }

    @DisplayName("Order를 생성할 수 있다.(OrderTable)")
    @Test
    void create01() {
        // given
        Order order = Order.from(테이블);

        // when & then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(OrderStatus.COOKING, order.getOrderStatus())
        );
    }

    @DisplayName("Order 생성 시 OrderTable이 없는 경우 예외가 발생한다.")
    @Test
    void create02() {
        // given & when & then
        assertThrows(CreateOrderException.class, () -> Order.from(null));
    }

    @DisplayName("Order 의 주문상태 OrderStatus를 변경할 수 있다.")
    @Test
    void change01() {
        // given
        Order order = Order.from(테이블);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertEquals(OrderStatus.MEAL, order.getOrderStatus());
    }

    @DisplayName("Order 의 주문상태가 완료상태이면 OrderStatus를 변경할 수 없다.")
    @Test
    void change02() {
        // given
        Order order = Order.from(테이블);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}