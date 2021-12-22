package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 도메인 테스트")
class OrderTest {

    final Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
    final OrderLineItem orderLineItem = OrderLineItem.of(menu, 2L);

    @Test
    @DisplayName("주문 진행중 여부")
    void isOnGoing() {
        Order order_1 = Order.of(1L, OrderStatus.COOKING, Arrays.asList(orderLineItem));
        Order order_2 = Order.of(1L, OrderStatus.COMPLETION, Arrays.asList(orderLineItem));
        Order order_3 = Order.of(1L, OrderStatus.MEAL, Arrays.asList(orderLineItem));

        assertAll(
            () -> assertTrue(order_1.isOnGoing()),
            () -> assertFalse(order_2.isOnGoing()),
            () -> assertTrue(order_3.isOnGoing())
        );
    }

    @Test
    @DisplayName("주문 생성")
    void create() {
        Order order = Order.of(1L, null, Arrays.asList(orderLineItem));
        assertTrue(order.isOnGoing());
    }

    @Test
    @DisplayName("주문 상태 변경")
    void updateOrderStatus() {
        Order order = Order.of(1L, null, Arrays.asList(orderLineItem));
        assertTrue(order.isOnGoing());

        order.updateOrderStatus(OrderStatus.COMPLETION);

        assertFalse(order.isOnGoing());

        assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("계산완료된 주문은 변경할 수 없습니다.");
    }
}