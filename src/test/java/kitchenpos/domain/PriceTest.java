package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceTest {

    @Test
    void create_fail_priceNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(null));
    }

    @Test
    void create_fail_priceNegative() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(BigDecimal.valueOf(-1)));
    }

}