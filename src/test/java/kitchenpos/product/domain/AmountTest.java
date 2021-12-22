package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.Message;
import org.junit.jupiter.api.Test;

class AmountTest {

    @Test
    void 금액은_0보다_작을_수_없다() {

        assertThatThrownBy(() -> {
            Amount.of(0);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.AMOUNT_IS_NOT_LESS_THAN_ZERO.getMessage());
    }

    @Test
    void 금액_더하기() {
        // given
        Amount amount1 = Amount.of(1000);
        Amount amount2 = Amount.of(2000);

        // when
        Amount add = Amount.add(amount1, amount2);

        // then
        assertThat(add).isEqualTo(Amount.of(3000));
    }

    @Test
    void 금액_곱하기() {

        // when
        Amount result = Amount.of(1000).multiply(2L);

        // then
        assertThat(result).isEqualTo(Amount.of(2000));
    }

    @Test
    void 금액이_정상적이지_않은_경우() {
        // then
        assertThatThrownBy(() -> {
            Amount.of(null);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.AMOUNT_PRICE_IS_NOT_EMPTY.getMessage());
    }

}