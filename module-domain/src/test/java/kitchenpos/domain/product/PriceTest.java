package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PriceTest {
    @DisplayName("가격 객체를 생성한다.")
    @ParameterizedTest(name = "{0} 값으로 가격 객체 생성한다.")
    @ValueSource(longs = {1L, 1000L, 10_000L})
    void create(long value) {
        Price price = Price.from(value);

        assertThat(price).isNotNull();
    }

    @DisplayName("0원 미만 값으로 가격을 생성할 수 없다.")
    @ParameterizedTest(name = "{0} 값은 0원 미만이다.")
    @ValueSource(longs = {-1L, -1000L, -10_000L})
    void createFail(long value) {
        assertThatThrownBy(() -> {
            Price price = Price.from(value);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("곱을 구할 수 있다.")
    @ParameterizedTest(name = "{0}과 {1}의 곱은 {2}이다.")
    @CsvSource(value = {"1000:3:3000", "21000:2:42000"}, delimiter = ':')
    void multiply(long value, long multiplyValue, BigDecimal expected) {
        Price price = Price.from(value);

        price.multiply(multiplyValue);

        assertThat(price.getPrice()).isEqualTo(expected);
    }

    @DisplayName("곱의 결과는 0보다 커야한다.")
    @ParameterizedTest(name = "{0}과 {1}의 곱은 0 미만이다.")
    @CsvSource(value = {"1000:-3", "21000:-2"}, delimiter = ':')
    void multiplyFail(long value, long multiplyValue) {
        Price price = Price.from(value);

        assertThatThrownBy(() -> {
            price.multiply(multiplyValue);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
