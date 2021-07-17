package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;

public class QuantityTest {

    @Test
    void given_NumberLessThanZero_when_CreateQuantity_then_ThrowException() {
        // given
        final long quantity = -1;

        // when
        final Throwable throwable = catchThrowable(() -> new Quantity(quantity));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
