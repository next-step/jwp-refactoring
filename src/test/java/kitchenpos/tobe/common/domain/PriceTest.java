package kitchenpos.tobe.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PriceTest {

    @DisplayName("상품 가격을 생성할 수 있다.")
    @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
    @ValueSource(strings = {"0", "16000"})
    void create(final BigDecimal price) {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Price(price);

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("상품 가격은 null이 될 수 없다.")
    @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
    @NullSource
    void createFailPriceNull(final BigDecimal price) {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Price(price);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void createFailPriceNegative() {
        // given
        final BigDecimal price = BigDecimal.valueOf(Long.MIN_VALUE);

        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Price(price);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격 간 동등성을 비교할 수 있다.")
    @Test
    void equals() {
        // given
        final BigDecimal price = BigDecimal.valueOf(16_000L);

        // when
        final Price price1 = new Price(price);
        final Price price2 = new Price(price);

        // then
        assertThat(price1).isEqualTo(price2);
    }
}
