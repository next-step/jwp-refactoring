package kitchenpos.common;

import kitchenpos.menu.exception.WrongPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("0보다 큰 수를 입력해야 한다.")
    @Test
    void bigThanZero() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(WrongPriceException.class);
    }

    @DisplayName("곱셈")
    @Test
    void multiply() {
        Price price = new Price(BigDecimal.valueOf(100));
        assertThat(price.multiply(25)).isEqualTo(new Price(BigDecimal.valueOf(2500)));
    }

}
