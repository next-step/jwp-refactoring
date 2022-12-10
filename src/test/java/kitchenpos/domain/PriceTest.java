package kitchenpos.domain;

import kitchenpos.common.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PriceTest {
    @DisplayName("메뉴 가격은 null 일 수 없다.")
    @Test
    void priceNullException() {
        assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 null 일 수 없습니다.");
    }

    @DisplayName("메뉴 가격은 0 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -100, -1000})
    void priceUnderZeroException(int value) {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(value)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격은 0 이상이어야 합니다.");
    }
}
