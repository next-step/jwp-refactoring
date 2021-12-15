package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.domain.table.OrderTable;

class OrderTest {

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = OrderTableFixtureFactory.create(1L, true);
    }

    @DisplayName("Oders 는 OrderTable 로 생성된다.")
    @Test
    void create1() {
        // when & then
        assertThatNoException().isThrownBy(() -> Order.from(주문테이블));
    }

    @DisplayName("Oders 생성 시, 0OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create2() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> Order.from(null))
                                            .withMessageContaining("OrderTable 이 존재하지 않습니다.");
    }

    @DisplayName("완료된 상태가 아니라면, Order 의 상태를 바꿀 수 있다.")
    @Test
    void changeOrderStatus1() {
        // given
        Order order = Order.from(주문테이블);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertTrue(order.getOrderStatus().isMeal());
    }

    @DisplayName("완료된 Order 의 상태를 바꾸면 예외가 발생한다.")
    @Test
    void changeOrderStatus2() {
        // given
        Order order = Order.from(주문테이블);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                                            .withMessageContaining("완료된 Order 는 상태를 바꿀 수 없습니다.");
    }
}