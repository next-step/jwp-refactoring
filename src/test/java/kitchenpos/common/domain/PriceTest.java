package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.ExceptionMessage;

class PriceTest {

    @DisplayName("가격은 null 이거나 0보다 작을 수 없다.")
    @Test
    void validatePrice() {
        assertAll(
            () -> assertThatThrownBy(() -> new Price(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage()),
            () -> assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WRONG_VALUE.getMessage())
        );
    }

    @DisplayName("가격이 총 가격보다 더 크다.")
    @Test
    void isGreaterThanSumPrice() {
        Price price = new Price(BigDecimal.valueOf(1000));
        assertTrue(price.isGreaterThanSumPrice(BigDecimal.valueOf(999)));
    }

    @DisplayName("가격이 총 가격보다 더 작거나 같다.")
    @ParameterizedTest
    @ValueSource(ints = {1000, 1001})
    void isLessThanEqualSumPrice(int sum) {
        Price price = new Price(BigDecimal.valueOf(1000));
        assertFalse(price.isGreaterThanSumPrice(BigDecimal.valueOf(sum)));
        assertFalse(price.isGreaterThanSumPrice(BigDecimal.valueOf(sum)));
    }

}
