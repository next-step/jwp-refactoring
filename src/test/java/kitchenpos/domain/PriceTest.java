package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.KitchenposException;

class PriceTest {
    @DisplayName("null로 가격 생성시 에러")
    @Test
    void constructErrorWhenNull() {
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> new Price(null))
            .withMessage("0 이상의 가격만 입력 가능합니다.");
    }

    @DisplayName("0 이하의 가격으로로 가격 생성시 에러")
    @Test
    void constructErrorWhenLessThanZero() {
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
            .withMessage("0 이상의 가격만 입력 가능합니다.");
    }
}