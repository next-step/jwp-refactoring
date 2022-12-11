package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest {
    @DisplayName("이름은 null 일 수 없다.")
    @Test
    void nameNullException() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.NAME_SHOULD_NOT_EMPTY.getMessage());
    }

    @DisplayName("이름은 빈 값일 수 없다.")
    @Test
    void emptyNameException() {
        assertThatThrownBy(() -> new Name(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.NAME_SHOULD_NOT_EMPTY.getMessage());
    }
}
