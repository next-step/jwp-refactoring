package domain.order;

import common.valueobject.exception.NegativeQuantityException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemTest {

    @Test
    void create() {
        //when
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1);

        //then
        assertThat(orderLineItem.getMenuId()).isEqualTo(1L);
        assertThat(orderLineItem.getQuantity().getValue()).isEqualTo(1);
    }

    @Test
    void registerOrder() {
        //given
        OrderLineItem orderLineItem = OrderLineItem.of(1L, 1);

        //when
        orderLineItem.registerOrder(2L);

        //then
        assertThat(orderLineItem.getOrderId()).isEqualTo(2L);
    }

    @DisplayName("주문할 때 메뉴 수량은 음수가 될 수 없다.")
    @Test
    void createOrderExceptionIfMenuQuantityIsNull() {
        //when
        Assertions.assertThatThrownBy(() -> OrderLineItem.of(null, -1))
                .isInstanceOf(NegativeQuantityException.class); //then
    }
}
