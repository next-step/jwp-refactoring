package kitchenpos.common.domain;

import kitchenpos.common.Price;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @ParameterizedTest
    @ValueSource(longs = {
            0, 1
    })
    @DisplayName("가격 생성 - 0보다 크거나 같은 수")
    void create(long price) {
        Price actual = new Price(BigDecimal.valueOf(price));

        assertThat(actual).isEqualTo(new Price(BigDecimal.valueOf(price)));
    }

    @Test
    @DisplayName("가격에 빈값이 들어왔을 경우")
    void nullPriceTest() {
        assertThatThrownBy(() -> new Price(null)).isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("가격이 음수일 경우")
    void negativePriceTest() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1))).isInstanceOf(InvalidPriceException.class);
    }
}