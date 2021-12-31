package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class NameTest {

    @DisplayName("이름을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final String name = "치킨";

        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Name(name);

        // then
        assertThatNoException().isThrownBy(request);
    }

    @DisplayName("이름은 null 또는 \"\"이 될 수 없다.")
    @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
    @NullAndEmptySource
    void createFailInvalidName(final String name) {
        // when
        final ThrowableAssert.ThrowingCallable request = () -> new Name(name);

        // then
        assertThatThrownBy(request).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름 간 동등성을 비교할 수 있다.")
    @Test
    void equals() {
        // given
        final String name = "치킨";

        // when
        final Name name1 = new Name(name);
        final Name name2 = new Name(name);

        // then
        assertThat(name1).isEqualTo(name2);
    }
}
