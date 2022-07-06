package order.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @DisplayName("주문항목을 생성한다.")
    @Test
    void create() {
        //given
        long menuId = 1L;
        int quantity = 4;

        //when
        OrderLineItem orderLineItem = new OrderLineItem(menuId, quantity);

        //then
        assertAll(
                () -> assertEquals(quantity, orderLineItem.getQuantity()),
                () -> assertEquals(menuId, orderLineItem.getMenuId())
        );
    }
}