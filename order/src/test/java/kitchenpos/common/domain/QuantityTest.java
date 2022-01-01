package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.Quantity;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class QuantityTest {

    @DisplayName("수량을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final long quantity = 1L;

        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Quantity(quantity);

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("수량은 1개 이상이어야 한다.")
    @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
    @ValueSource(longs = {0L, Long.MIN_VALUE})
    void createFailQuantityNotPositive(final long quantity) {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Quantity(quantity);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("수량 간 동등성을 비교할 수 있다.")
    @Test
    void equals() {
        // given
        final long quantity = 1L;

        // when
        final Quantity quantity1 = new Quantity(quantity);
        final Quantity quantity2 = new Quantity(quantity);

        // then
        assertThat(quantity1).isEqualTo(quantity2);
    }
}
