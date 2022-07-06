package kitchenpos.domain.common.wrap;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Domain:Wrap:Price")
class PriceTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("상품 가격이 유효하지 않은 경우 예외 발생 검증")
    public void throwException_WhenProductPriceIsLessThanZero(final BigDecimal given, final String testDescription) {
        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Price.productPriceFrom(given));
    }

    private static Stream<Arguments> throwException_WhenProductPriceIsLessThanZero() {
        BigDecimal nullBigDecimal = null;
        return Stream.of(
            Arguments.of(nullBigDecimal, "상품 가격 정보가 없는 경우"),
            Arguments.of(new BigDecimal(Integer.MIN_VALUE), "상품 가격 정보가 음수인 경우")
        );
    }

    @Test
    @DisplayName("메뉴 가격이, 메뉴에 구성된 상품 가격 총합보다 큰 경우 예외")
    public void throwException_WhenMenuPriceIsOverThanMenuProductsTotalPrice() {
        // Given
        final BigDecimal givenMenuProductsPrice = new BigDecimal(10000);
        final BigDecimal givenMenuPrice = givenMenuProductsPrice.add(new BigDecimal(1));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> Price.menuPriceOf(givenMenuPrice, givenMenuProductsPrice));
    }
}
