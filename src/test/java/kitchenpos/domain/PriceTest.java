package kitchenpos.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Price 테스트")
public class PriceTest {
    @DisplayName("0 보다 큰 수를 입력해야 한다")
    @Test
    void testGreaterThanZero() {
        // when
        ThrowableAssert.ThrowingCallable callable = () -> Price.of(0);

        // then
        assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
    }
}
