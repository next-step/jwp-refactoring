package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("테이블 도메인 테스트")
class OrderTableTest {

    @Test
    @DisplayName("테이블 상태 변경")
    void updateEmpty() {
        OrderTable orderTable = OrderTable.of(1, false);
        assertFalse(orderTable.isEmpty());

        orderTable.updateEmpty(true);
        assertTrue(orderTable.isEmpty());
    }

    @DisplayName("단체 지정된 테이블의 상태는 변경 불가")
    @Test
    void validateUpdateEmpty() {
        OrderTable orderTable = OrderTable.of(1, false);
        ReflectionTestUtils.setField(orderTable, "table_group_id", 1L);

        assertThatThrownBy(() -> orderTable.updateEmpty(Boolean.TRUE))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("단체지정된 테이블은 변경할 수 없습니다.");
    }

    @DisplayName("주문 진행중인 테이블의 상태는 변경 불가")
    @Test
    void validateUpdateEmptyOnGoingOrder() {
        final OrderTable orderTable = OrderTable.of(1, false);
        final Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
        final OrderLineItem orderLineItem = OrderLineItem.of(menu, 2L);

        Order.of(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem));

        assertThatThrownBy(() -> orderTable.updateEmpty(Boolean.TRUE))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("주문이 완료되지 않은 테이블이 있습니다.");
    }

    @Test
    @DisplayName("테이블의 손님 수 변경")
    void updateNumberOfGuests() {
        OrderTable orderTable = OrderTable.of(1, true);

        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(2))
            .isInstanceOf(CannotUpdatedException.class)
            .hasMessage("빈 테이블의 손님수는 변경 할 수 없습니다.");

        orderTable.updateEmpty(false);
        orderTable.updateNumberOfGuests(2);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(2);
    }
}