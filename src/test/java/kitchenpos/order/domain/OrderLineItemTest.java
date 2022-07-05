package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 항목")
class OrderLineItemTest {

    @DisplayName("같은 메뉴를 가졌는지 확인할 수 있다.")
    @Test
    void hasSameMenu() {
        Menu 메뉴 = new Menu("메뉴", BigDecimal.ZERO, new MenuGroup("그룹"), new MenuProducts());
        OrderLineItem 주문_항목 = OrderLineItem.of(메뉴, 1L);
        OrderLineItem 같은_메뉴의_주문_항목2 = OrderLineItem.of(메뉴, 2L);

        assertThat(주문_항목.hasSameMenu(같은_메뉴의_주문_항목2)).isTrue();
    }
}
