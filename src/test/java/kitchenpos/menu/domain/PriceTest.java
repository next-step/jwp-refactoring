package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PriceTest {

    @DisplayName("가격 도메인 유효성 체크 테스트")
    @ParameterizedTest
    @MethodSource("providePrice")
    void validate(BigDecimal price) {
        // when
        final Throwable throwable = catchThrowable(() -> new Price(price));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> providePrice() {
        return Stream.of(
            Arguments.of((Object)null),
            Arguments.of(new BigDecimal(-1))
        );
    }

    @DisplayName("숫자 크기를 비교하는 테스트 ")
    @Test
    void greaterThan() {
        // given
        final Price price = new Price(new BigDecimal(100));

        // when
        final boolean actual = price.greaterThan(new BigDecimal(10));

        // then
        assertThat(actual).isEqualTo(true);
    }
}
