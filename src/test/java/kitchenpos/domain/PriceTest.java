package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceTest {

    @DisplayName("가격 생성 작업을 성공한다.")
    @Test
    void of() {
        // when
        Price price = Price.from(BigDecimal.ONE);

        // then
        assertThat(price.value()).isEqualTo(BigDecimal.ONE);
    }

    @DisplayName("가격 생성 할 때, null이면 IllegalArgumentException을 반환한다.")
    @Test
    void ofWithException1() {
        // when & then
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 생성 할 때, 0보다 작으면 IllegalArgumentException을 반환한다.")
    @Test
    void ofWithException2() {
        // when & then
        assertThatThrownBy(() -> Price.from(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("곱셈 작업에 성공한다.")
    @Test
    void multiply() {
        // when
        Price price = Price.from(BigDecimal.ONE).multiply(Quantity.from(5));

        // then
        assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(5)));
    }

    @DisplayName("덧셈 작업에 성공한다.")
    @Test
    void add() {
        // when
        Price price = Price.from(BigDecimal.ONE);

        // then
        assertThat(price.add(Price.from(BigDecimal.TEN))).isEqualTo(Price.from(BigDecimal.valueOf(11)));
    }
}
