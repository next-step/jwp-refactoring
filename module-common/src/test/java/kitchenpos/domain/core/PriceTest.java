package kitchenpos.domain.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PriceTest {

    @ParameterizedTest(name = "{0} 일 경우")
    @MethodSource("providerCreateFailCase")
    void createFail(BigDecimal value, Class<? extends Exception> exception) {
        assertThatExceptionOfType(exception)
            .isThrownBy(() -> new Price(value));
    }

    @Test
    @DisplayName("입력값보다 더 큰지 체크한다.")
    void isGreaterThan() {
        Price price = new Price(BigDecimal.ONE);
        assertThat(price.isGreaterThan(BigDecimal.ZERO)).isTrue();
    }

    private static Stream<Arguments> providerCreateFailCase() {
        return Stream.of(
            Arguments.of(null, NullPointerException.class),
            Arguments.of(BigDecimal.valueOf(-1), IllegalArgumentException.class)
        );
    }

}
