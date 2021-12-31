package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final String name = "치킨";
        final BigDecimal price = BigDecimal.valueOf(16_000L);

        // when
        final ThrowableAssert.ThrowingCallable request = () -> ProductFixture.of(
            new Name(name),
            new Price(price)
        );

        // then
        assertThatNoException().isThrownBy(request);
    }
}
