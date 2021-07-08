package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
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
}
