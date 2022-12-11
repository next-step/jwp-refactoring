package kitchenpos.domain;

import kitchenpos.common.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {
    @DisplayName("이름은 null 일 수 없다.")
    @Test
    void nameNullException() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 값이 존재해야 합니다.");
    }

    @DisplayName("이름은 빈 값일 수 없다.")
    @Test
    void emptyNameException() {
        assertThatThrownBy(() -> new Name(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 값이 존재해야 합니다.");
    }
}
