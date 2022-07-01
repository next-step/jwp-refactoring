package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @ParameterizedTest
    @DisplayName("음수는 가격으로 생성할수 없다.")
    @ValueSource(ints = {-1, -2})
    public void cantCreatePriceWithMinusOrNullTest(int source) {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(source)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @DisplayName("음수는 가격으로 설정할수 없다.")
    @ValueSource(ints = {-1, -2})
    public void cantSetPriceWithMinusOrNullTest(int source) {
        Price price = new Price(BigDecimal.valueOf(3));
        assertThatThrownBy(() -> price.changePrice(BigDecimal.valueOf(source)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:false", "9:false", "7:true", "6:true"}, delimiter = ':')
    @DisplayName("크고 작음을 구분할수 있다.")
    public void compareNumberTest(int source, boolean expected) {
        Price price = new Price(BigDecimal.valueOf(8));

        assertThat(price.bigger(BigDecimal.valueOf(source))).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"3:6", "4:8", "5:10"}, delimiter = ':')
    @DisplayName("값을 곱할수 있다.")
    public void multiplyTest(int multSource, int expected) {
        Price price = new Price(BigDecimal.valueOf(2));
        assertThat(price.multiply(BigDecimal.valueOf(multSource))).isEqualTo(
            BigDecimal.valueOf(expected));

    }
}