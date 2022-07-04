package kitchenpos.common.domain;

import kitchenpos.common.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceTest {

    @DisplayName("가격을 생성한다.")
    @Test
    void create() {
        //given
        BigDecimal value = BigDecimal.valueOf(1000);

        //when
        Price price = new Price(value);

        //then
        assertEquals(value, price.getValue());
    }

    @DisplayName("가격은 null일 수 없다.")
    @Test
    void create_fail_priceNull() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(null));
    }

    @DisplayName("가격은 음수 일 수 없다.")
    @Test
    void create_fail_priceNegative() {
        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> new Price(BigDecimal.valueOf(-1)));
    }

    @DisplayName("비교값보다 큰지 확인한다.")
    @CsvSource(value = {"999,false", "1000,false", "1001,true"})
    @ParameterizedTest
    void greaterThan(long priceValue, boolean expected) {
        //given
        BigDecimal compare = BigDecimal.valueOf(1000);
        Price price = new Price(BigDecimal.valueOf(priceValue));

        //when
        boolean isGreaterThan = price.greaterThan(compare);

        //then
        assertEquals(expected, isGreaterThan);
    }
}