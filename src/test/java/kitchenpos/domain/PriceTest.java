package kitchenpos.domain;

import kitchenpos.common.exceptions.NegativePriceException;
import kitchenpos.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("가격 도메인 테스트")
class PriceTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThat(Price.from(BigDecimal.ONE)).isEqualTo(Price.from(BigDecimal.ONE));
    }

    @DisplayName("가격이 최소값(0) 이상이어야 한다")
    @Test
    void validateTest1() {
        final BigDecimal number = BigDecimal.valueOf(-1);
        assertThatThrownBy(() -> Price.from(number))
                .isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("가격이 null 이면 안된다")
    @Test
    void validateTest2() {
        assertThatThrownBy(() -> Price.from(null))
                .isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("곱할 수 있다")
    @Test
    void multiplyTest() {
        final Price price = Price.valueOf(2);
        final Long multiplyNumber = 3L;
        final Price expected = Price.valueOf(6);

        final Price actual = price.multiply(multiplyNumber);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("더할 수 있다")
    @Test
    void addTest() {
        final Price price = Price.valueOf(2);
        final Price addPrice = Price.valueOf(3);
        final Price expected = Price.valueOf(5);

        final Price actual = price.add(addPrice);

        assertThat(actual).isEqualTo(expected);
    }

}