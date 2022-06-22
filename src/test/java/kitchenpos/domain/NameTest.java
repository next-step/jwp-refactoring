package kitchenpos.domain;

import static kitchenpos.exception.EmptyNameException.CANT_EMPTY_IS_NAME;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.exception.EmptyNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @DisplayName("Name을 생성할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = {"123", "1", "444", "asdf", "ababab", "ff!@$!@$!@#$"})
    void create01(String name) {
        assertThatNoException().isThrownBy(() -> Name.from(name));
    }

    @DisplayName("Name을 생성할 수 없다. (null or empty)")
    @ParameterizedTest
    @NullAndEmptySource
    void create02(String name) {
        assertThatExceptionOfType(EmptyNameException.class)
                .isThrownBy(() -> Name.from(name))
                .withMessageContaining(String.format(CANT_EMPTY_IS_NAME, name));
    }
}