package domain.order;

import common.valueobject.exception.NegativeQuantityException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @DisplayName("주문할 때 메뉴 수량은 음수가 될 수 없다.")
    @Test
    void createOrderExceptionIfMenuQuantityIsNull() {
        //when
        Assertions.assertThatThrownBy(() -> OrderLineItem.of(null, -1))
                .isInstanceOf(NegativeQuantityException.class); //then
    }
}
