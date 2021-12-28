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

    @DisplayName("가격이 더 큰지 확인")
    @Test
    void isBiggerThanReturnTrue() {
        Price one = new Price(BigDecimal.ONE);
        Price zero = new Price(BigDecimal.ZERO);

        assertThat(one.isBiggerThan(zero)).isTrue();
    }

    @DisplayName("가격이 더 크지 않은지 확인")
    @Test
    void isBiggerThanReturnFalse() {
        Price one = new Price(BigDecimal.ONE);
        Price zero = new Price(BigDecimal.ZERO);

        assertThat(zero.isBiggerThan(one)).isFalse();
    }

    @DisplayName("가격 곱하기")
    @Test
    void multiply() {
        Price ten = new Price(BigDecimal.TEN);

        assertThat(ten.multiply(BigDecimal.valueOf(5))).isEqualTo(new Price(BigDecimal.valueOf(50)));
    }

    @Test
    void add() {
        Price one = new Price(BigDecimal.ONE);
        Price ten = new Price(BigDecimal.TEN);

        assertThat(Price.add(one, ten)).isEqualTo(new Price(BigDecimal.valueOf(11)));
    }
}