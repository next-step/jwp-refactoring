package kitchenpos.common.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("가격 테스트")
class PriceTest {

    @DisplayName("가격 생성 성공 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> price: {0}")
    @ValueSource(ints = {0, 1_000})
    void instantiate_success(int value) {
        // given
        BigDecimal price = BigDecimal.valueOf(value);

        // when & then
        assertThat(Price.of(price)).isExactlyInstanceOf(Price.class);
    }

    @DisplayName("가격 생성 실패 테스트 - 음수")
    @Test
    void instantiate_success_negative() {
        // given
        BigDecimal price = BigDecimal.valueOf(-1);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(price));
    }

    @DisplayName("가격 생성 실패 테스트 - null")
    @Test
    void instantiate_failure_null() {
        // given
        BigDecimal price = null;

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(price));
    }

    @DisplayName("sum 테스트")
    @Test
    void sum() {
        // given
        BigDecimal 천원 = BigDecimal.valueOf(1_000);
        BigDecimal 오백원 = BigDecimal.valueOf(500);

        // when
        Price price = Price.sum(Lists.list(천원, 오백원, 오백원));

        // then
        assertThat(price).isEqualTo(Price.of(BigDecimal.valueOf(2_000)));
    }

    @DisplayName("multiply 테스트")
    @Test
    void multiply() {
        // given
        Price price = Price.of(BigDecimal.valueOf(1_000));

        // when
        Price result = price.multiply(BigDecimal.TEN);

        // then
        assertThat(result).isEqualTo(Price.of(BigDecimal.valueOf(10_000)));
    }

    @DisplayName("isMoreExpensive 테스트 - 저렴함")
    @Test
    void isMoreExpensive_cheap() {
        // given
        Price price = Price.of(BigDecimal.valueOf(1_000));

        // when
        boolean result = price.isMoreExpensive(Price.of(BigDecimal.valueOf(2_000)));

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("isMoreExpensive 테스트 - 같음")
    @Test
    void isMoreExpensive_same() {
        // given
        Price price = Price.of(BigDecimal.valueOf(1_000));

        // when
        boolean result = price.isMoreExpensive(Price.of(BigDecimal.valueOf(1_000)));

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("isMoreExpensive 테스트 - 비쌈")
    @Test
    void isMoreExpensive_expensive() {
        // given
        Price price = Price.of(BigDecimal.valueOf(1_000));

        // when
        boolean result = price.isMoreExpensive(Price.of(BigDecimal.valueOf(500)));

        // then
        assertThat(result).isTrue();
    }
}
