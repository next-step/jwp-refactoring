package kitchenpos.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static kitchenpos.common.Name.NAME_NOT_EMPTY_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("이름")
class NameTest {

    @DisplayName("이름을 생성한다.")
    @Test
    void constructor() {
    }

    @DisplayName("이름은 null 일 수 없다. / 이름은 공백일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void constructor_fail(String name) {
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NAME_NOT_EMPTY_EXCEPTION_MESSAGE);
    }
}
