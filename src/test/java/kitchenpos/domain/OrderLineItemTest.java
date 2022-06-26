package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderLineItemTest {

    @DisplayName("주문항목을 생성한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup(1L, "menuGroup");
        Menu menu = new Menu(1L, "menu", BigDecimal.valueOf(1000), menuGroup);
        int quantity = 4;

        //when
        OrderLineItem orderLineItem = new OrderLineItem(menu, quantity);

        //then
        assertAll(
                () -> assertEquals(quantity, orderLineItem.getQuantity()),
                () -> assertEquals(menu.getId(), orderLineItem.getMenu().getId())
        );
    }
}