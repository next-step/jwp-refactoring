package kitchenpos.menu.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.jpa.domain.JpaSort;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    private Price price;

    @BeforeEach
    void setUp() {
        price = Price.of(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("가격이 null 이 입력되면 exception 이 발생함")
    void throwExceptionMenuPriceIsNull() {
        assertThatThrownBy(() -> Price.of(null)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("가격이 음수가 입력되면 exception이 발생함")
    void throwExceptionMenuPriceIsNegative() {
        assertThatThrownBy(() -> Price.of(BigDecimal.valueOf(-1))).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("price와 같거나 큰 값이 전달되면 true")
    @ValueSource(ints = {1000,1001})
    void isLessOrEqualTo(Integer trueValue) {
        boolean lessOrEqualTo = price.isLessOrEqualTo(BigDecimal.valueOf(trueValue));

        assertThat(lessOrEqualTo).isTrue();
    }

    @Test
    @DisplayName("price보다 큰 값이 입력되면 false")
    void isBiggerThanPrice() {
        boolean lessOrEqualTo = price.isLessOrEqualTo(BigDecimal.valueOf(999));

        assertThat(lessOrEqualTo).isFalse();
    }

    @Test
    @DisplayName("곱하기 계산을 수행")
    void multiply() {
        Price multiply = price.multiply(2);

        assertThat(multiply).isEqualTo(Price.of(BigDecimal.valueOf(2000)));
    }
}