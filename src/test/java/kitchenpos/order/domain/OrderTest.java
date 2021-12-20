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
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 도메인 테스트")
class OrderTest {

    final OrderTable orderTable = OrderTable.of(2, false);
    final Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
    final OrderLineItem orderLineItem = OrderLineItem.of(menu, 2L);
    @Test
    @DisplayName("주문 진행중 여부")
    void isOnGoing() {
        Order order_1 = Order.of(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem));
        Order order_2 = Order.of(orderTable, OrderStatus.COMPLETION, Arrays.asList(orderLineItem));
        Order order_3 = Order.of(orderTable, OrderStatus.MEAL, Arrays.asList(orderLineItem));

        assertAll(
            () -> assertTrue(order_1.isOnGoing()),
            () -> assertFalse(order_2.isOnGoing()),
            () -> assertTrue(order_3.isOnGoing())
        );
    }

    @Test
    @DisplayName("주문 생성")
    void create() {
        Order order = Order.of(orderTable, null, Arrays.asList(orderLineItem));
        assertTrue(order.isOnGoing());
    }

    @Test
    @DisplayName("테이블 정보는 필수, 빈 테이블인 경우 주문을 생성할 수 없다.")
    void createValidateEmptyTable() {
        assertThatThrownBy(() -> Order.of(null, null, Arrays.asList(orderLineItem)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("테이블은 필수입니다.");

        assertThatThrownBy(() -> Order.of(OrderTable.of(0, true), null, Arrays.asList(orderLineItem)))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("빈 테이블은 주문을 할 수 없습니다.");

    }

    @Test
    @DisplayName("주문 상태 변경")
    void updateOrderStatus() {
        Order order = Order.of(orderTable, null, Arrays.asList(orderLineItem));
        assertTrue(order.isOnGoing());

        order.updateOrderStatus(OrderStatus.COMPLETION);

        assertFalse(order.isOnGoing());

        assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("계산완료된 주문은 변경할 수 없습니다.");
    }
}