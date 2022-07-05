package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    @DisplayName("주문 항목을 추가할 수 있다.")
    @Test
    void add() {
        Menu 메뉴 = new Menu("메뉴", BigDecimal.ZERO, new MenuGroup("그룹"), new MenuProducts());
        OrderLineItems 주문_항목들 = new OrderLineItems();
        주문_항목들.add(OrderLineItem.of(메뉴, 1L));

        assertThat(주문_항목들.value()).isNotEmpty();
    }

    @DisplayName("주문 항목 리스트가 비어 있는지 확인할 수 있다.")
    @Test
    void isEmpty() {
        OrderLineItems 주문_항목들 = new OrderLineItems();
        assertThat(주문_항목들.isEmpty()).isTrue();
    }
}
