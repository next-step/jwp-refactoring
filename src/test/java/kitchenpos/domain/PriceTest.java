package kitchenpos.domain;

import static kitchenpos.exception.NegativePriceException.INVALID_PRICE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import kitchenpos.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @DisplayName("Price을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {1L, 100L, 200L, 1000L, 10000L})
    void create01(long price) {
        assertThatNoException().isThrownBy(() -> Price.from(BigDecimal.valueOf(price)));
    }

    @DisplayName("Price을 생성할 수 없다. (0 이하의 값)")
    @ParameterizedTest
    @ValueSource(longs = {-10000L, -1L, 0})
    void create02(long price) {
        assertThatExceptionOfType(NegativePriceException.class)
                .isThrownBy(() -> Price.from(BigDecimal.valueOf(price)))
                .withMessageContaining(String.format(INVALID_PRICE, price));
    }

}