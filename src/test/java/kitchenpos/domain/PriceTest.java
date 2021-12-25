package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {
    @DisplayName("0보다 작은 가격은 생성할 수 없다.")
    @Test
    void validate() {
        assertThatThrownBy(
                () -> Price.of(new BigDecimal(-1))
        ).isInstanceOf(InvalidPriceException.class);
    }
}
