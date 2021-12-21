package kitchenpos.product.domain;

import kitchenpos.common.exception.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AmountTest {

    @Test
    void 금액은_0보다_작을_수_없다() {

        Assertions.assertThatThrownBy(() -> {
                Amount.of(0);
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.AMOUNT_IS_NOT_LESS_THAN_ZERO.getMessage());
    }

}