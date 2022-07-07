package kitchenpos.domain.product.wrap;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.product.domain.wrap.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:Product:Wrap:Price")
class PriceTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("상품 가격이 유효하지 않은 경우 예외 발생 검증")
    public void throwException_WhenProductPriceIsLessThanZero(final BigDecimal given, final String testDescription) {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Price.from(given));
    }

    private static Stream<Arguments> throwException_WhenProductPriceIsLessThanZero() {
        BigDecimal nullBigDecimal = null;
        return Stream.of(
            Arguments.of(nullBigDecimal, "상품 가격 정보가 없는 경우"),
            Arguments.of(new BigDecimal(Integer.MIN_VALUE), "상품 가격 정보가 음수인 경우")
        );
    }
}
