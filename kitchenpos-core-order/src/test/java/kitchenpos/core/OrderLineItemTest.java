package kitchenpos.core;

import kitchenpos.core.domain.OrderLineItem;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderLineItemTest {

    @DisplayName("수량은 0이하일 경우 생성할 수 없다.")
    @Test
    void create() {
        // given
        final int quantity = -12;
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> OrderLineItem.of(1L, quantity);
        // then
        assertThatIllegalArgumentException().isThrownBy(throwingCallable);
    }

}
