package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {
    @Test
    @DisplayName("가격 생성")
    void createPrice() {
        // when
        Price actual = Price.from(BigDecimal.valueOf(8000));

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Price.class)
        );
    }

    @Test
    @DisplayName("가격은 비어있을 수 없다.")
    void createPriceByNull() {
        // when & then
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 필수입니다.");
    }

    @ParameterizedTest(name = "[{index}] 가격은 0원 이상이어야 한다.")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void createPriceByNegative(long value) {
        // when & then
        assertThatThrownBy(() -> Price.from(BigDecimal.valueOf(value)))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("가격을 더한다")
    void addPrice() {
        // given
        Price one = Price.from(BigDecimal.ONE);
        Price ten = Price.from(BigDecimal.TEN);

        // when
        Price actual = ten.sum(one);

        // then
        assertThat(actual).isEqualTo(Price.from(BigDecimal.valueOf(11)));
    }

    @Test
    @DisplayName("가격을 곱한다")
    void multiplyPrice() {
        // given
        Price one = Price.from(BigDecimal.valueOf(2));

        // when
        Price actual = one.multiply(BigDecimal.valueOf(10));

        // then
        assertThat(actual).isEqualTo(Price.from(BigDecimal.valueOf(20)));
    }
}
