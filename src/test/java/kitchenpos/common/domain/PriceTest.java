package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("가격")
class PriceTest {

    @DisplayName("생성")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 생성 가능")
    @ValueSource(longs = {1, Long.MAX_VALUE})
    void instance(long value) {
        assertThatNoException()
            .isThrownBy(() -> Price.from(BigDecimal.valueOf(value)));
    }

    @Test
    @DisplayName("가격 값은 필수")
    void instance_nullValue_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Price.from(null))
            .withMessage("가격 값은 필수입니다.");
    }

    @DisplayName("가격은 반드시 양수")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 생성 불가능")
    @ValueSource(longs = {-1, Long.MIN_VALUE})
    void instance_negativeValue_thrownIllegalArgumentException(long value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Price.from(BigDecimal.valueOf(value)))
            .withMessageEndingWith("반드시 0이상 이어야 합니다.");
    }

    @Test
    @DisplayName("합하기")
    void sum() {
        //given
        Price price = Price.from(BigDecimal.ONE);

        //when
        Price sum = price.sum(Price.from(BigDecimal.ONE));

        //then
        assertThat(sum).isEqualTo(Price.from(BigDecimal.valueOf(2)));
    }

    @ParameterizedTest(name = "[{index}] 10은 {1} 보다 작거나 같음 여부가 {1}")
    @CsvSource({"1,false", "10,true", "11,true"})
    @DisplayName("같거나 이하 여부")
    void equalOrLessThan(long value, boolean expected) {
        //given
        Price price = Price.from(BigDecimal.TEN);

        //when
        boolean equalOrLessThan = price.equalOrLessThan(Price.from(BigDecimal.valueOf(value)));

        //then
        assertThat(equalOrLessThan).isEqualTo(expected);
    }

    @Test
    @DisplayName("곱하기")
    void multiply() {
        //given
        Price price = Price.from(BigDecimal.ONE);

        //when
        Price multiply = price.multiply(10);

        //then
        assertThat(multiply).isEqualTo(Price.from(BigDecimal.TEN));
    }
}
