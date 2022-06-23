package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("가격은 0원 이상이어야 한다.")
    void pricePositiveTest() {

        assertThatThrownBy(
                () -> new Price(BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
