package kitchenpos.tobe.orders.domain.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.tobe.fixture.OrderLineItemFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {

    @DisplayName("주문 항목을 생성할 수 있다.")
    @Test
    void create() {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> OrderLineItemFixture.of(1L, 1L, 1L);

        // then
        assertThatNoException().isThrownBy(request);
    }
}
