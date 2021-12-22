package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목 도메인 테스트")
class OrderLineItemTest {

    @Test
    @DisplayName("동등성 비교")
    void equalsOrderLineItem() {
        Menu menu = Menu.of("후라이드치킨", 10000, MenuGroup.from("치킨"));
        OrderLineItem orderLineItem_1 = OrderLineItem.of(menu, 2L);
        OrderLineItem orderLineItem_2 = OrderLineItem.of(menu, 1L);

        assertFalse(orderLineItem_1.equalsOrderLineItem(orderLineItem_2));
    }

    @Test
    @DisplayName("메뉴는 필수")
    void createValidateMenu() {
        assertThatThrownBy(() -> OrderLineItem.of(null, 2L))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("메뉴는 필수 입니다.");
    }
}