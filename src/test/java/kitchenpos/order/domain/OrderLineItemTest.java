package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class OrderLineItemTest {

    @Test
    @DisplayName("동등성 비교")
    void equalsOrderLineItem() {
        OrderTable orderTable = OrderTable.of(2, false);
        Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
        OrderLineItem orderLineItem_1 = OrderLineItem.of(menu, 2L);
        OrderLineItem orderLineItem_2 = OrderLineItem.of(menu, 2L);

        assertFalse(orderLineItem_1.equalsOrderLineItem(orderLineItem_2));

        Order order = Order.of(orderTable, OrderStatus.COOKING, Arrays.asList(orderLineItem_1, orderLineItem_2));
        ReflectionTestUtils.setField(order, "id", 1L);

        assertTrue(orderLineItem_1.equalsOrderLineItem(orderLineItem_2));
    }

    @Test
    @DisplayName("메뉴는 필수")
    void createValidateMenu() {
        assertThatThrownBy(() -> OrderLineItem.of(null, 2L))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("메뉴는 필수 입니다.");
    }
}