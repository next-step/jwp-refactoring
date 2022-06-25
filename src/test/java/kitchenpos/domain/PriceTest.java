package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격은 null일 수 없다.")
    @Test
    void create_fail_priceNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(null));
    }

    @DisplayName("가격은 음수 일 수 없다.")
    @Test
    void create_fail_priceNegative() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(BigDecimal.valueOf(-1)));
    }

}