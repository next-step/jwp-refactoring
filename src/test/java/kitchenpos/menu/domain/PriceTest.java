package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.common.Message.ERROR_PRICE_REQUIRED;
import static kitchenpos.common.Message.ERROR_PRICE_SHOULD_BE_OVER_THAN_ZERO;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {

    @DisplayName("가격이 비어있는 경우, 예외발생")
    @Test
    void 가격_비어있는_경우_예외발생() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_PRICE_REQUIRED.showText());
    }

    @DisplayName("가격이 음수인 경우, 예외발생")
    @Test
    void 가격_음수인_경우_예외발생() {
        BigDecimal 음수 = BigDecimal.valueOf(-1000);

        assertThatThrownBy(() -> new Price(음수))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_PRICE_SHOULD_BE_OVER_THAN_ZERO.showText());
    }
}
