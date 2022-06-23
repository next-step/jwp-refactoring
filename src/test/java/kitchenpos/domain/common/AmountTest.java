package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountTest {

    @Test
    @DisplayName("금액은 0원 이상이어야 합니다.")
    void validationTest() {
        assertThatThrownBy(
                () -> new Amount(BigDecimal.valueOf(-1))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
