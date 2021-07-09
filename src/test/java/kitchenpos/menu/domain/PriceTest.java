package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("0보다 작은 값을 입력하면 예외가 발생한다.")
    @Test
    void validateValue() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1);

        // when & then
        assertThatThrownBy(() -> Price.from(price))
                .isInstanceOf(IllegalArgumentException.class);
    }
}