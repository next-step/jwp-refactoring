package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PriceTest {
    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Price price = Price.from(BigDecimal.TEN);
        assertThat(price.value()).isEqualTo(BigDecimal.TEN);
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.from(null))
                .withMessage("금액을 지정해야 합니다.");
    }

    @DisplayName("최소값 아래 경우 테스트")
    @Test
    void ofWithUnderMin() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.from(BigDecimal.valueOf(-100)))
                .withMessage("금액은 " + Price.MIN + "원 미만이 될 수 없습니다.");
    }

    @DisplayName("금액 끼리는 더할 수 있다.")
    @ParameterizedTest(name = "금액 {0}은 {1}와 더해 {2}이 된다.")
    @CsvSource(value = {"1000:2000:3000", "100:0:100", "10000:10:10010"}, delimiter = ':')
    void add(int input, int other, int expect) {
        Price price = Price.from(BigDecimal.valueOf(input));
        price.add(Price.from(BigDecimal.valueOf(other)));
        assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(expect)));
    }

    @DisplayName("금액은 수량과 곱할 수 있다.")
    @ParameterizedTest(name = "금액 {0}은 수량 {1}와 곱해 금액 {2}이 된다.")
    @CsvSource(value = {"1000:20:20000", "100:0:0", "10000:10:100000"}, delimiter = ':')
    void multiply(int input, int other, int expect) {
        Price price = Price.from(BigDecimal.valueOf(input));
        Price multiply = price.multiply(Quantity.from(other));
        assertThat(multiply).isEqualTo(Price.from(BigDecimal.valueOf(expect)));
    }
}
