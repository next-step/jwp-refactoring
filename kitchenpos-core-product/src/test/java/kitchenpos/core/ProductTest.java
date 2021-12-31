package kitchenpos.core;

import kitchenpos.core.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("가격은 0보다 작을 경우 에러가 발생한다.")
    @Test
    void price() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> Product.of("쌀국수", -1);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 빈 값일 수 없다.")
    @Test
    void priceEmpty() {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> Product.of("쌀국수", null);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "이름은 빈값이 될 수 없다. \"{0}\"")
    @NullAndEmptySource
    void nameEmpty(String name) {
        // given, when
        ThrowableAssert.ThrowingCallable createCall = () -> Product.of(name, null);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
